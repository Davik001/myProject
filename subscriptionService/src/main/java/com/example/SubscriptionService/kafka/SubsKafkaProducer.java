package com.example.SubscriptionService.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SubsKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(SubsKafkaProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.subscription-events}")
    private String subscriptionEventsTopic;

    public SubsKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Отправка сообщения в топик subscription-events
    public void sendNotificationTask(Long subscriptionId, Long customerId, String eventType, String productDetails) {
        try {
            // Формируем объект сообщения (можно сделать отдельный DTO, если нужно)
            String message = String.format(
                    "{\"subscriptionId\": %d, \"customerId\": %d, \"eventType\": \"%s\", \"productDetails\": \"%s\"}",
                    subscriptionId, customerId, eventType, productDetails
            );

            kafkaTemplate.send(subscriptionEventsTopic, message);
            log.info("Отправлено сообщение в топик {}: {}", subscriptionEventsTopic, message);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения в Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось отправить задачу в Kafka", e);
        }
    }
}

