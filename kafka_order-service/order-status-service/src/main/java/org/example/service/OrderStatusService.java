package org.example.service;

import org.examples.model.StatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusService {


    @Value(value = "${spring.kafka.kafkaMessageTopic}")
    private String staticTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderStatusService(@Qualifier("myKafka") KafkaTemplate<String, Object> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendStaticMessage(StatusMessage message) {

        kafkaTemplate.send(staticTopic, message);
    }
}
