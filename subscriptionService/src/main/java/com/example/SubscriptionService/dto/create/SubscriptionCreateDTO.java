package com.example.SubscriptionService.dto.create;

import com.example.shared.EventType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionCreateDTO {
    private Long customerId;
    private Long productId;
    private EventType eventType;
}