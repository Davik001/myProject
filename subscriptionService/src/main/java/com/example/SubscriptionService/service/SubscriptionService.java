package com.example.SubscriptionService.service;


import com.example.SubscriptionService.CrmCustomer;
import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.dto.create.SubscriptionCreateDTO;
import com.example.SubscriptionService.dto.update.SubscriptionUpdateDTO;
import com.example.SubscriptionService.entity.Subscription;
import com.example.SubscriptionService.map.SubscriptionMapper;
import com.example.SubscriptionService.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CrmCustomer crmCustomer;
    private final SubscriptionMapper subscriptionMapper;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, CrmCustomer crmCustomer, SubscriptionMapper subscriptionMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.crmCustomer = crmCustomer;
        this.subscriptionMapper = subscriptionMapper;
    }

    // создание. Для API
    public SubscriptionDTO createSubscription(SubscriptionCreateDTO dto) {
        // Проверка в CRM
        ResponseEntity<Void> productResponse = crmCustomer.checkProductExists(dto.getProductId());
        if (!productResponse.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("Product with ID " + dto.getProductId() + " does not exist in CRM");
        }

        ResponseEntity<Void> customerResponse = crmCustomer.checkCustomerExists(dto.getCustomerId());
        if (!customerResponse.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("Customer with ID " + dto.getCustomerId() + " does not exist in CRM");
        }

        // Маппинг DTO -> Entity
        Subscription subscription = subscriptionMapper.toEntity(dto);
        Subscription saved = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDto(saved);
    }

    // обновление
    public SubscriptionDTO updateSubscription(Long id, SubscriptionUpdateDTO dto) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        // Проверка в CRM, если обновляются productId или customerId
        if (dto.getProductId() != null) {
            ResponseEntity<Void> productResponse = crmCustomer.checkProductExists(dto.getProductId());
            if (!productResponse.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Product with ID " + dto.getProductId() + " does not exist in CRM");
            }
        }

        if (dto.getCustomerId() != null) {
            ResponseEntity<Void> customerResponse = crmCustomer.checkCustomerExists(dto.getCustomerId());
            if (!customerResponse.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Customer with ID " + dto.getCustomerId() + " does not exist in CRM");
            }
        }

        // Маппинг DTO -> Entity с обновлением существующей сущности
        Subscription updatedSubscription = subscriptionMapper.toEntity(dto);
        updateSubscriptionFields(subscription, updatedSubscription);

        Subscription saved = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDto(saved);
    }

    private void updateSubscriptionFields(Subscription target, Subscription source) {
        if (source.getProductId() != null) {
            target.setProductId(source.getProductId());
        }
        if (source.getCustomerId() != null) {
            target.setCustomerId(source.getCustomerId());
        }
        if (source.getEventType() != null) {
            target.setEventType(source.getEventType());
        }
    }

    // Удаление подписки
    public void deleteSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscriptionRepository.delete(subscription);
    }

    // Просмотр подписок
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    public SubscriptionDTO getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        return subscriptionMapper.toDto(subscription);
    }

    public List<SubscriptionDTO> getSubscriptionsByCustomer(Long customerId) {
        return subscriptionRepository.findByCustomerId(customerId).stream()
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    // Создание дефолтной подписки
    public SubscriptionDTO createDefaultSubscription(Long productId, Long customerId) {
        SubscriptionCreateDTO dto = new SubscriptionCreateDTO();
        dto.setProductId(productId);
        dto.setCustomerId(customerId);
        dto.setEventType("DEFAULT_EVENT");

        Subscription subscription = subscriptionMapper.toEntity(dto);
        Subscription saved = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDto(saved);
    }
}