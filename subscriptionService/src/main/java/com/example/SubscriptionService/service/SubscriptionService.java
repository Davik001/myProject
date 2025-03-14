package com.example.SubscriptionService.service;


import com.example.SubscriptionService.CrmCustomer;
import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.dto.create.SubscriptionCreateDTO;
import com.example.SubscriptionService.dto.update.SubscriptionUpdateDTO;
import com.example.SubscriptionService.entity.Subscription;
import com.example.SubscriptionService.kafka.SubsKafkaProducer;
import com.example.SubscriptionService.map.SubscriptionMapper;
import com.example.SubscriptionService.repository.SubscriptionRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final CrmCustomer crmCustomer;
    private final SubscriptionMapper subscriptionMapper;
    private final SubsKafkaProducer kafkaProducer;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               CrmCustomer crmCustomer,
                               SubscriptionMapper subscriptionMapper,
                               SubsKafkaProducer kafkaProducer) {
        this.subscriptionRepository = subscriptionRepository;
        this.crmCustomer = crmCustomer;
        this.subscriptionMapper = subscriptionMapper;
        this.kafkaProducer = kafkaProducer;
    }

    // Создание подписки
    @Transactional
    public SubscriptionDTO createSubscription(SubscriptionCreateDTO dto) {
        log.info("Создание подписки для customerId: {}, productId: {}", dto.getCustomerId(), dto.getProductId());

        // Проверяем существование клиента и продукта в CRM
        checkCrmEntities(dto.getCustomerId(), dto.getProductId());

        // Создаём подписку
        Subscription subscription = subscriptionMapper.toEntity(dto);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        SubscriptionDTO result = subscriptionMapper.toDto(savedSubscription);

        // Отправляем задачу в Kafka для микросервиса уведомлений
        kafkaProducer.sendNotificationTask(
                result.getId(),
                result.getCustomerId(),
                result.getEventType(),
                "Подписка создана"
        );

        log.info("Подписка успешно создана с ID: {}", result.getId());
        return result;
    }

    // Обновление подписки
    @Transactional
    public SubscriptionDTO updateSubscription(Long id, SubscriptionUpdateDTO dto) {
        log.info("Обновление подписки с ID: {}", id);

        Subscription existingSubscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Подписка с ID " + id + " не найдена"));

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
                .orElseThrow(() -> new RuntimeException("Подписка с ID " + id + " не найдена"));
        subscriptionRepository.delete(subscription);

        log.info("Подписка с ID: {} успешно удалена", id);
    }

    // Просмотр всех подписок
    public List<SubscriptionDTO> getAllSubscriptions() {
        log.info("Получение списка всех подписок");
        List<SubscriptionDTO> subscriptions = subscriptionRepository.findAll()
                .stream()
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toList());
        log.info("Найдено подписок: {}", subscriptions.size());
        return subscriptions;
    }

    // Просмотр подписки по ID
    public SubscriptionDTO getSubscriptionById(Long id) {
        log.info("Получение подписки с ID: {}", id);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Подписка с ID " + id + " не найдена"));
        return subscriptionMapper.toDto(subscription);
    }

    // Просмотр подписок клиента
    public List<SubscriptionDTO> getSubscriptionsByCustomer(Long customerId) {
        log.info("Получение подписок для клиента с ID: {}", customerId);
        List<SubscriptionDTO> subscriptions = subscriptionRepository.findByCustomerId(customerId)
                .stream()
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toList());
        log.info("Найдено подписок для клиента {}: {}", customerId, subscriptions.size());
        return subscriptions;
    }

    // Создание дефолтной подписки
    @Transactional
    public SubscriptionDTO createDefaultSubscription(Long productId, Long customerId) {
        log.info("Создание дефолтной подписки для customerId: {}, productId: {}", customerId, productId);

        // Проверяем существование клиента и продукта
        checkCrmEntities(customerId, productId);

        SubscriptionCreateDTO dto = new SubscriptionCreateDTO();
        dto.setCustomerId(customerId);
        dto.setProductId(productId);
        dto.setEventType("PRODUCT_PRICE_CHANGE"); // Дефолтное событие
        SubscriptionDTO result = createSubscription(dto);

        log.info("Дефолтная подписка успешно создана с ID: {}", result.getId());
        return result;
    }

    // Проверка клиента и продукта в CRM
    private void checkCrmEntities(Long customerId, Long productId) {
        log.info("Проверка существования клиента {} и продукта {} в CRM", customerId, productId);

        ResponseEntity<Void> customerResponse = crmCustomer.checkCustomerExists(customerId);
        if (!customerResponse.getStatusCode().is2xxSuccessful()) {
            log.error("Клиент с ID {} не существует в CRM", customerId);
            throw new RuntimeException("Клиент с ID " + customerId + " не существует в CRM");
        }

        ResponseEntity<Void> productResponse = crmCustomer.checkProductExists(productId);
        if (!productResponse.getStatusCode().is2xxSuccessful()) {
            log.error("Продукт с ID {} не существует в CRM", productId);
            throw new RuntimeException("Продукт с ID " + productId + " не существует в CRM");
        }

        log.info("Клиент {} и продукт {} успешно проверены в CRM", customerId, productId);
    }

    // Обновление полей подписки
    private void updateSubscriptionFields(Subscription target, Subscription source) {
        target.setCustomerId(source.getCustomerId());
        target.setProductId(source.getProductId());
        target.setEventType(source.getEventType());
    }
}