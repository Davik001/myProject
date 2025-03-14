package com.example.SubscriptionService.dto.alldtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionDTO {
    private Long id;
    private Long customerId;
    private Long productId;
    private String eventType;
    private LocalDateTime createdAt;

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getProductId() {
        return productId;
    }
    public String getEventType() {
        return eventType;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Long getId() {
        return id;
    }
    public Long getCustomerId() {
        return customerId;
    }

}