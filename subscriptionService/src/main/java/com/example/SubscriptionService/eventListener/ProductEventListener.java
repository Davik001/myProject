package com.example.SubscriptionService.eventListener;

import com.example.SubscriptionService.dto.create.SubscriptionCreateDTO;
import com.example.SubscriptionService.event.ProductAddedEvent;
import com.example.SubscriptionService.service.SubscriptionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {

    private final SubscriptionService subscriptionService;

    public ProductEventListener(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @KafkaListener(topics = "product-added", groupId = "subscriptions-group")
    public void handleProductAdded(ProductAddedEvent event) {
        SubscriptionCreateDTO dto = new SubscriptionCreateDTO();
        dto.setClientId(event.getDefaultCustomerId());
        dto.setProductId(event.getProductId());
        dto.setEventType("DEFAULT_EVENT"); // Дефолтное событие, например, "PRICE_CHANGE"
        subscriptionService.createSubscription(dto);
    }
}