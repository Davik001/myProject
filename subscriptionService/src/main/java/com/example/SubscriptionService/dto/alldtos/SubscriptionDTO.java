package com.example.SubscriptionService.dto.alldtos;

import com.example.SubscriptionService.EventType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {
    private Long id;
    private Long customerId;
    private Long productId;
    private EventType eventType;
    private LocalDateTime createdAt;
}