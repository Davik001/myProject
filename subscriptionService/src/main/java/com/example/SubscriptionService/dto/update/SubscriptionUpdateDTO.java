package com.example.SubscriptionService.dto.update;

import lombok.Data;

@Data
public class SubscriptionUpdateDTO {
    private Long customerId;
    private Long productId;
    private String eventType;

    // Геттеры
    public Long getCustomerId() {
        return customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getEventType() {
        return eventType;
    }

    // Сеттеры
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}