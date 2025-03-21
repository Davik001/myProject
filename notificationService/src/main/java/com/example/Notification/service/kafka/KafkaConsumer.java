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

    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumer(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${spring.kafka.topics.subscription-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void readSubscription(String message) {
            kafkaTemplate.send("subscription-events", "Подписка создана!");
    }
}
