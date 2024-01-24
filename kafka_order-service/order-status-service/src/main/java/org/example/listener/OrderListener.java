package org.example.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.OrderStatusService;
import org.examples.model.OrderEvent;
import org.examples.model.StatusMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

    private final OrderStatusService orderStaticService;

    private static String STATUS = "CREATED";

    @KafkaListener(topics = "${spring.kafka.kafkaOrderTopic}", groupId = "${spring.kafka.kafkaMessageGroupId}")
    public void listener(@Payload OrderEvent order,
                         @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                         @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {

        log.info("Received order: order {}, key {}, partition {}, topic {}, timestamp {}", order, key, partition, topic, timestamp);

        orderStaticService.sendStaticMessage(new StatusMessage(STATUS, Instant.now()));
    }


}
