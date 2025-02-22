package com.example.Notification.service.dto.create;

import lombok.Data;

@Data
public class SubscriptionCreateDTO {
    private Long clientId;
    private Long productId;
    private String eventType;
}