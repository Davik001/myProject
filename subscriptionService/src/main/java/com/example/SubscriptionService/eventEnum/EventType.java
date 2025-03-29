package com.example.SubscriptionService.eventEnum;

import lombok.Getter;

@Getter
public enum EventType {
    CREATED("Создан"),
    UPDATE("Изменено имя."),
    DELETE("Объект удален."),
    PRICE_INCREASE("Цена поднялась!"),
    PRICE_DECREASE("Цена падает!");

    private final String details;

    EventType(String details) {
        this.details = details;
    }
}
