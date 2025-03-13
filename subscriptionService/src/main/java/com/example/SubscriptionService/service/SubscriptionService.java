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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

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
        // Проверяем существование клиента и продукта в CRM
        checkCrmEntities(dto.getCustomerId(), dto.getProductId());

        // Создаём подписку
        Subscription subscription = subscriptionMapper.toEntity(dto);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDto(savedSubscription);
    }

    // Обновление подписки
    @Transactional
    public SubscriptionDTO updateSubscription(Long id, SubscriptionUpdateDTO dto) {
        Subscription existingSubscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Подписка с ID " + id + " не найдена"));

        // Проверяем новые значения клиента и продукта, если они изменились
        checkCrmEntities(dto.getCustomerId(), dto.getProductId());

        // Обновляем поля
        updateSubscriptionFields(existingSubscription, subscriptionMapper.toEntity(dto));
        Subscription updatedSubscription = subscriptionRepository.save(existingSubscription);
        return subscriptionMapper.toDto(updatedSubscription);
    }

    // Удаление подписки
    @Transactional
    public void deleteSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Подписка с ID " + id + " не найдена"));
        subscriptionRepository.delete(subscription);
    }

    // Просмотр всех подписок
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionRepository.findAll()
                .stream()
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    // Просмотр подписки по ID
    public SubscriptionDTO getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Подписка с ID " + id + " не найдена"));
        return subscriptionMapper.toDto(subscription);
    }

    // Просмотр подписок клиента
    public List<SubscriptionDTO> getSubscriptionsByCustomer(Long customerId) {
        return subscriptionRepository.findByCustomerId(customerId)
                .stream()
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    // Создание дефолтной подписки
    @Transactional
    public SubscriptionDTO createDefaultSubscription(Long productId, Long customerId) {
        // Проверяем существование клиента и продукта
        checkCrmEntities(customerId, productId);

        SubscriptionCreateDTO dto = new SubscriptionCreateDTO();
        dto.setCustomerId(customerId);
        dto.setProductId(productId);
        dto.setEventType("PRODUCT_PRICE_CHANGE"); // Дефолтное событие
        return createSubscription(dto);
    }

    // Проверка клиента и продукта в CRM
    private void checkCrmEntities(Long customerId, Long productId) {
        ResponseEntity<Void> customerResponse = crmCustomer.checkCustomerExists(customerId);
        if (!customerResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Клиент с ID " + customerId + " не существует в CRM");
        }

        ResponseEntity<Void> productResponse = crmCustomer.checkProductExists(productId);
        if (!productResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Продукт с ID " + productId + " не существует в CRM");
        }
    }

    // Обновление полей подписки
    private void updateSubscriptionFields(Subscription target, Subscription source) {
        target.setCustomerId(source.getCustomerId());
        target.setProductId(source.getProductId());
        target.setEventType(source.getEventType());
    }
}