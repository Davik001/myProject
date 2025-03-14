package com.example.SubscriptionService.kafka;

import com.example.SubscriptionService.CrmCustomer;
import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.entity.Subscription;
import com.example.SubscriptionService.repository.SubscriptionRepository;
import com.example.SubscriptionService.service.SubscriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


@Component
public class SubsKafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(SubsKafkaConsumer.class);

    private final SubscriptionService subscriptionService;
    private final SubsKafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public SubsKafkaConsumer(SubscriptionService subscriptionService,
                             SubsKafkaProducer kafkaProducer,
                             ObjectMapper objectMapper) {
        this.subscriptionService = subscriptionService;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${spring.kafka.topics.product-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenProductEvents(String message) {
        try {
            log.info("Получено сообщение из топика product-events: {}", message);

            // Парсим сообщение из JSON в объект ProductEvent
            ProductEvent productEvent = objectMapper.readValue(message, ProductEvent.class);
            Long productId = productEvent.getProductId();
            String productDetails = productEvent.getDetails();

            // Находим все подписки, связанные с продуктом
            List<SubscriptionDTO> subscriptions = subscriptionService.getAllSubscriptions()
                    .stream()
                    .filter(sub -> sub.getProductId().equals(productId))
                    .toList();

            if (subscriptions.isEmpty()) {
                log.info("Подписки для продукта с ID {} не найдены", productId);
                return;
            }

            // Обрабатываем каждую подписку
            for (SubscriptionDTO subscription : subscriptions) {
                log.info("Обрабатывается подписка ID {} для клиента {}", subscription.getId(), subscription.getCustomerId());
                // Отправляем задачу в микросервис уведомлений
                kafkaProducer.sendNotificationTask(
                        subscription.getId(),
                        subscription.getCustomerId(),
                        subscription.getEventType(),
                        productDetails
                );
            }

        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения из Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие продукта", e);
        }
    }
}
