package com.example.SubscriptionService.kafka;

import com.example.SubscriptionService.CrmFeignClient;
import com.example.SubscriptionService.dto.alldtos.ProductDTO;
import com.example.shared.EventType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class SubsKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.subscription-events}")
    private String subscriptionEventsTopic;

    public void sendNotificationTask(Long subscriptionId, Long customerId, EventType eventType, Map<String, String> productDetails) {
        try {
            String message = String.format(
                    "Уведомление для подписки #%d (Клиент #%d): %s. Детали: %s",
                    subscriptionId, customerId, eventType.getDetails(), productDetails
            );

            kafkaTemplate.send(subscriptionEventsTopic, message);
            log.info("Отправлено сообщение в топик {}: {}", subscriptionEventsTopic, message);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения в Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось отправить задачу в Kafka", e);
        }
    }
}