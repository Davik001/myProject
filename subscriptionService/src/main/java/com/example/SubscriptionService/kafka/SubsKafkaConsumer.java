package com.example.SubscriptionService.kafka;

import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.eventEnum.EventType;
import com.example.SubscriptionService.service.SubscriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;


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
            String productDetails = productEvent.getDetails();

            // Получаем все подписки на продукт
            List<SubscriptionDTO> subscriptions = subscriptionService.getSubscriptionsByProductId(productId);

            if (subscriptions.isEmpty()) {
                log.info("Нет подписок на продукт с ID {}", productId);
                return;
            }

            // Обрабатываем каждую подписку
            for (SubscriptionDTO subscription : subscriptions) {
                log.info("Обрабатываем подписку ID {} для клиента {}", subscription.getId(), subscription.getCustomerId());

                kafkaProducer.sendNotificationTask(
                        subscription.getId(),
                        subscription.getCustomerId(),
                        subscription.getEventType().name(),
                        productDetails
                );
            }

        } catch (Exception e) {
            log.error("Ошибка при обработке события из Kafka: {}", e.getMessage(), e);
        }
    }
}
