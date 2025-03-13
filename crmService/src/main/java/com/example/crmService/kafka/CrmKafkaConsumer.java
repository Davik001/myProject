package com.example.crmService.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CrmKafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(CrmKafkaConsumer.class);

}
