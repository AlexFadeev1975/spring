package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;
import org.examples.model.OrderEvent;
import org.examples.model.StatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

@Slf4j
public class OrderService {

    @Value(value = "${spring.kafka.kafkaOrderTopic}")
    private String topic;


    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderService(@Qualifier("myKafka") KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private final List<Order> orders = new ArrayList<>();

    private final List<StatusMessage> messages = new ArrayList<>();

    public void add(Order order) {

        orders.add(order);

        OrderEvent event = new OrderEvent();
        event.setProduct(order.getProduct());
        event.setQuantity(order.getQuantity().toString());


        kafkaTemplate.send(topic, event);

        log.info("Order {} sent to topic {}", event, topic);
    }

    public void addMessage(StatusMessage message) {

        messages.add(message);
    }
}
