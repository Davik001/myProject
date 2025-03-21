package com.example.crmService.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CrmKafkaConsumer {
    private final Logger log = LoggerFactory.getLogger(CrmKafkaConsumer.class);
    private final ObjectMapper objectMapper;

    @Autowired
    public CrmKafkaConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${spring.kafka.topics.subscription-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenSubscriptionEvents(String message) {
        try {
            log.info("Получено сообщение из топика subscription-events: {}", message);

            // Парсим сообщение из JSON в объект SubscriptionEvent
            SubscriptionEvent subscriptionEvent = objectMapper.readValue(message, SubscriptionEvent.class);

            // Здесь можно добавить логику обработки события
            log.info("Получено подтверждение подписки: subscriptionId={}, customerId={}, eventType={}, details={}",
                    subscriptionEvent.getSubscriptionId(),
                    subscriptionEvent.getCustomerId(),
                    subscriptionEvent.getEventType(),
                    subscriptionEvent.getDetails());

        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения из Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие подписки", e);
        }
    }

}
