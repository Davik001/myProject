package com.example.SubscriptionService.kafka;

import com.example.SubscriptionService.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {
    Long productId;
    String details;
    EventType eventType;
}
