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
}