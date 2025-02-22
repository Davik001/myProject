package com.example.SubscriptionService.event;

import lombok.Data;

@Data
public class ProductAddedEvent { // для десериализации сообщений из кафки
    private Long productId;          // ID нового продукта
    private Long defaultCustomerId;  // ID клиента, для которого создаётся дефолтная подписка
}