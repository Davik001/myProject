package com.example.SubscriptionService.kafka;

import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.service.SubscriptionService;
import com.example.shared.EventType;
import com.example.shared.ProductEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class SubsKafkaConsumer {
    private final SubscriptionService subscriptionService;
    private final SubsKafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topics.product-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenProductEvents(String message) {
        try {
            log.info("Получено сообщение из топика product-events: {}", message);

            // Парсим сообщение в объект ProductEvent
            ProductEvent productEvent = objectMapper.readValue(message, ProductEvent.class);
            Long productId = productEvent.getProductId();
            EventType eventType = productEvent.getEventType();
            Map<String, String> productDetails = productEvent.getDetails();

            // Фильтрация подписок по продукту и типу события
            List<SubscriptionDTO> subscriptions = subscriptionService.getSubscriptionsByProductIdAndEventType(productId, eventType);

            if (subscriptions.isEmpty()) {
                log.info("Нет подписок на продукт {} с событием {}", productId, eventType);
                return;
            }

            for (SubscriptionDTO subscription : subscriptions) {
                log.info("Отправляем уведомление клиенту {} по подписке {} на событие {}",
                        subscription.getCustomerId(), subscription.getId(), eventType);

                kafkaProducer.sendNotificationTask(
                        subscription.getId(),
                        subscription.getCustomerId(),
                        eventType,
                        productDetails
                );
            }

        } catch (Exception e) {
            log.error("Ошибка при обработке события из Kafka: {}", e.getMessage(), e);
        }
    }
}
