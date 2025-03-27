package com.example.Notification.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final KafkaTemplate<String, SubscriptionEvent> kafkaTemplate;

    @Autowired
    public KafkaConsumer(KafkaTemplate<String, SubscriptionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${spring.kafka.topics.subscription-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void readSubscription(String message) {
        logger.info("Получено сообщение из Kafka: {}", message);

        // Создаем корректный JSON-объект, а не просто строку
        SubscriptionEvent event = new SubscriptionEvent();
        event.setEventType("SUBSCRIPTION_CONFIRMED");
        event.setDetails("Подписка успешно создана!");

        kafkaTemplate.send("subscription-request", event);
        logger.info("Отправлено сообщение обратно в Kafka: {}", event);
    }
}
