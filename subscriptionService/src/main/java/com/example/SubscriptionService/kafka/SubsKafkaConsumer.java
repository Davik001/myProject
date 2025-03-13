package com.example.SubscriptionService.kafka;

import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.entity.Subscription;
import com.example.SubscriptionService.repository.SubscriptionRepository;
import com.example.SubscriptionService.service.SubscriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class SubsKafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(SubsKafkaConsumer.class);

}
