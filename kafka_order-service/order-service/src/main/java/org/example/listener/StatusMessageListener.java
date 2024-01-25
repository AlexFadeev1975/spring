package org.example.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.services.OrderService;
import org.examples.model.StatusMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class StatusMessageListener {

    private final OrderService orderService;


    @KafkaListener(topics = "${spring.kafka.kafkaMessageTopic}",
                   groupId = "${spring.kafka.kafkaMessageGroupId}",
                   containerFactory = "messageKafkaListenerContainerFactory")
    public void listener(@Payload StatusMessage message,
                         @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                         @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {

        log.info("Received message: {}", message);
        log.info(" Key: {}, Partition: {}, Topic: {}, Timestamp: {}", key, partition, topic, timestamp);

        orderService.addMessage(message);


    }


}
