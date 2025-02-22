package com.example.SubscriptionService.service;


import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.dto.create.SubscriptionCreateDTO;
import com.example.SubscriptionService.dto.update.SubscriptionUpdateDTO;
import com.example.SubscriptionService.entity.Subscription;
import com.example.SubscriptionService.map.SubscriptionMapper;
import com.example.SubscriptionService.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final SubscriptionMapper mapper;
    private final KafkaTemplate<String, Object> kafkaTemplate; // Добавляем KafkaTemplate

    public SubscriptionService(SubscriptionRepository repository,
                               SubscriptionMapper mapper,
                               KafkaTemplate<String, Object> kafkaTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public SubscriptionDTO createSubscription(SubscriptionCreateDTO dto) {
        Subscription subscription = mapper.toEntity(dto);
        Subscription saved = repository.save(subscription);
        return mapper.toDto(saved);
    }

    public SubscriptionDTO updateSubscription(Long id, SubscriptionUpdateDTO dto) {
        Subscription existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        Subscription updated = mapper.toEntity(dto);
        updated.setId(existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());
        Subscription saved = repository.save(updated);
        return mapper.toDto(saved);
    }

    public void deleteSubscription(Long id) {
        Subscription existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        repository.delete(existing);
    }

    public SubscriptionDTO getSubscriptionById(Long id) {
        Subscription subscription = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        return mapper.toDto(subscription);
    }

    public List<SubscriptionDTO> getSubscriptionsByCustomerId(Long customerId) {
        List<Subscription> subscriptions = repository.findByCustomerId(customerId);
        return subscriptions.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}