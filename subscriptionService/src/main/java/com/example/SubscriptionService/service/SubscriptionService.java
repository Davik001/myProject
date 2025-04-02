package com.example.SubscriptionService.service;


import com.example.SubscriptionService.CrmFeignClient;
import com.example.shared.EventType;
import com.example.SubscriptionService.dto.alldtos.ProductDTO;
import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.dto.create.SubscriptionCreateDTO;
import com.example.SubscriptionService.dto.update.SubscriptionUpdateDTO;
import com.example.SubscriptionService.entity.Subscription;
import com.example.SubscriptionService.exception.CustomEntityNotFoundException;
import com.example.SubscriptionService.kafka.SubsKafkaProducer;
import com.example.SubscriptionService.map.SubscriptionMapper;
import com.example.SubscriptionService.repository.SubscriptionRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final CrmFeignClient crmFeignClient;
    private final SubscriptionMapper subscriptionMapper;
    private final SubsKafkaProducer kafkaProducer;

    // Создание подписки
    @Transactional
    public SubscriptionDTO createSubscription(SubscriptionCreateDTO dto) {
        log.info("Запрос на создание подписки для customerId: {}, productId: {}", dto.getCustomerId(), dto.getProductId());
            // Проверяем существование клиента и продукта в CRM
            checkCrmEntities(dto.getCustomerId(), dto.getProductId());

            // Создаём подписку
            Subscription subscription = subscriptionMapper.toEntity(dto);
            Subscription savedSubscription = subscriptionRepository.save(subscription);
            SubscriptionDTO result = subscriptionMapper.toDto(savedSubscription);

            log.info("EventType в result: {}", result.getEventType());

            // формируем детали
       // Map<String, String> productDetails = Map.of("productId", dto.getProductId().toString());

        ResponseEntity<ProductDTO> response = crmFeignClient.getProductById(dto.getProductId());
        ProductDTO product = response.getBody();
        Map<String, String> productDetails = Map.of("Продукт ", product.getName());

        // Отправляем
        kafkaProducer.sendNotificationTask(
                result.getId(),
                result.getCustomerId(),
                result.getEventType(),
                product.getName(),
                productDetails,
                product.getPrice()
        );

            log.info("Подписка успешно создана с ID: {}", result.getId());
            return result;
    }


    // Обновление подписки
    @Transactional
    public SubscriptionDTO updateSubscription(Long id, SubscriptionUpdateDTO dto) {
        log.info("Обновление подписки с ID: {}", id);

        Subscription existingSubscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Подписка с ID " + id + " не найдена"));

        // Проверяем новые значения клиента и продукта, если они изменились
        checkCrmEntities(dto.getCustomerId(), dto.getProductId());

        // Обновляем поля
        updateSubscriptionFields(existingSubscription, subscriptionMapper.toEntity(dto));
        Subscription updatedSubscription = subscriptionRepository.save(existingSubscription);
        SubscriptionDTO result = subscriptionMapper.toDto(updatedSubscription);

        log.info("Подписка с ID: {} успешно обновлена", id);
        return result;
    }

    // Удаление подписки
    @Transactional
    public void deleteSubscription(Long id) {
        log.info("Удаление подписки с ID: {}", id);

        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Подписка с ID " + id + " не найдена"));
        subscriptionRepository.delete(subscription);

        log.info("Подписка с ID: {} успешно удалена", id);
    }

    // Просмотр всех подписок
    public List<SubscriptionDTO> getAllSubscriptions() {
        log.info("Получение списка всех подписок");

        try {
            List<Subscription> subscriptions = subscriptionRepository.findAll();
            log.info("Из БД получено записей: {}", subscriptions.size());

            List<SubscriptionDTO> dtoList = subscriptions.stream()
                    .map(subscriptionMapper::toDto)
                    .collect(Collectors.toList());

            log.info("Сконвертировано подписок в DTO: {}", dtoList.size());
            return dtoList;
        } catch (Exception e) {
            log.error("Ошибка при получении подписок", e);
            throw new IllegalArgumentException("Что-то случилось", e);
        }
    }

    // Просмотр подписки по ID
    public SubscriptionDTO getSubscriptionById(Long id) {
        log.info("Получение подписки с ID: {}", id);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Подписка с ID " + id + " не найдена"));
        return subscriptionMapper.toDto(subscription);
    }

    // Просмотр подписок клиента
    public List<SubscriptionDTO> getSubscriptionsByCustomer(Long customerId) {

        try {
            log.info("Получение подписок для клиента с ID: {}", customerId);
            List<SubscriptionDTO> subscriptions = subscriptionRepository.findByCustomerId(customerId)
                    .stream()
                    .map(subscriptionMapper::toDto)
                    .collect(Collectors.toList());
            log.info("Найдено подписок для клиента {}: {}", customerId, subscriptions.size());
            return subscriptions;
        } catch (Exception e) {
            throw new IllegalArgumentException("Что то случилось", e);
        }
    }

    // Проверка клиента и продукта в CRM
    public void checkCrmEntities(Long customerId, Long productId) {
        log.info("Проверка существования клиента {} и продукта {} в CRM", customerId, productId);

        ResponseEntity<Void> customerResponse = crmFeignClient.checkCustomerExists(customerId);
        if (!customerResponse.getStatusCode().is2xxSuccessful()) {
            log.error("Клиент с ID {} не существует в CRM", customerId);
            throw new CustomEntityNotFoundException("Клиент с ID " + customerId + " не существует в CRM");
        }

        ResponseEntity<Void> productResponse = crmFeignClient.checkProductExists(productId);
        if (!productResponse.getStatusCode().is2xxSuccessful()) {
            log.error("Продукт с ID {} не существует в CRM", productId);
            throw new CustomEntityNotFoundException("Продукт с ID " + productId + " не существует в CRM");
        }

        log.info("Клиент {} и продукт {} успешно проверены в CRM", customerId, productId);
    }

    // Обновление полей подписки
    private void updateSubscriptionFields(Subscription target, Subscription source) {
        target.setCustomerId(source.getCustomerId());
        target.setProductId(source.getProductId());
        target.setEventType(source.getEventType());
    }

    // Получение подписок по productId
    public List<SubscriptionDTO> getSubscriptionsByProductId(Long productId) {
        log.info("Получение подписок для продукта {}", productId);
        return subscriptionRepository.findByProductId(productId)
                .stream()
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    // Получение подписок по productId и eventType
    public List<SubscriptionDTO> getSubscriptionsByProductIdAndEventType(Long productId, EventType eventType) {
        log.info("Получение подписок для продукта {} с событием {}", productId, eventType);
        return subscriptionRepository.findByProductIdAndEventType(productId, eventType)
                .stream()
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toList());
    }
}