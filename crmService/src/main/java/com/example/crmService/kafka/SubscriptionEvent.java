package com.example.crmService.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEvent {
    private Long subscriptionId;
    private Long customerId;
    private String eventType;
    private String details;
}