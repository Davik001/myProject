package com.example.Notification.service.dto.update;

import lombok.Data;

@Data
public class SubscriptionUpdateDTO {
    private Long clientId;
    private Long productId;
    private String eventType;
}