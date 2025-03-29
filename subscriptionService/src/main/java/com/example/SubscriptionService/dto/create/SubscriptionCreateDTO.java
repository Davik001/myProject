package com.example.SubscriptionService.dto.create;

import com.example.SubscriptionService.eventEnum.EventType;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.aspectj.bridge.IMessage;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionCreateDTO {
    private Long customerId;
    private Long productId;
    private EventType eventType;
}