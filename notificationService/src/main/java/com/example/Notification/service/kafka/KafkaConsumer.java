package com.example.Notification.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.example.shared.SubscriptionEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final KafkaTemplate<String, SubscriptionEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumer(KafkaTemplate<String, SubscriptionEvent> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${spring.kafka.topics.subscription-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void readSubscription(String message) {
        try {
            log.info("Получено сообщение из Kafka: {}", message);
        } catch (Exception e) {
            log.error("Ошибка при обработке события из Kafka: {}", e.getMessage(), e);
        }
    }
}
