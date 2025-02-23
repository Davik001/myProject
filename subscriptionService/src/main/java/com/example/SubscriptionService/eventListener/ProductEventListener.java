package com.example.SubscriptionService.eventListener;

import com.example.SubscriptionService.event.ProductCreatedEvent;
import com.example.SubscriptionService.service.SubscriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductEventListener {

    private final SubscriptionService subscriptionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProductEventListener(SubscriptionService subscriptionService, ObjectMapper objectMapper) {
        this.subscriptionService = subscriptionService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "product-created", groupId = "subs-group")
    public void handleProductCreatedEvent(String message) {
        try {
            ProductCreatedEvent event = objectMapper.readValue(message, ProductCreatedEvent.class);
            Long productId = event.getProductId();
            Long customerId = event.getCustomerId();
            subscriptionService.createDefaultSubscription(productId, customerId);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse product-created event: " + message, e);
        }
    }
}