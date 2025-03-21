package com.example.crmService.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CrmKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(CrmKafkaProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productEventsTopic;

    @Autowired
    public CrmKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate,
                            @Value("${spring.kafka.topics.product-events}") String productEventsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.productEventsTopic = productEventsTopic;
    }

    public void sendProductEvent(ProductEvent productEvent) {
        try {
            kafkaTemplate.send(productEventsTopic, String.valueOf(productEvent.getProductId()), productEvent);
            log.info("Отправлено сообщение в топик {}: {}", productEventsTopic, productEvent);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения в Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось отправить событие продукта", e);
        }
    }
}
