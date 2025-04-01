package com.example.SubscriptionService.dto.update;

import com.example.shared.EventType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionUpdateDTO {
    private Long customerId;
    private Long productId;
    private EventType eventType;
}