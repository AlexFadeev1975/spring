package org.example.services.test;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.assertj.core.api.Assertions;
import org.examples.configurations.KafkaConfiguration;
import org.examples.model.OrderEvent;
import org.examples.model.StatusMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = {KafkaConfiguration.class})
@Testcontainers
public class OrderControllerTest {
    @Autowired
    @Qualifier("orderEvent")
    KafkaTemplate<String, OrderEvent> kafkaTemplate;

    static Network NETWORK = Network.newNetwork();
    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.3"))
            .withExposedPorts(9092, 9192, 9093)
            .withNetwork(NETWORK)
            .withNetworkAliases("kafka");
    @Container
    static final GenericContainer<?> orderStatusServiceContainer = new GenericContainer<>("order-status-service:latest")
            .withEnv("BS_SERVERS", "kafka:9092")
            .withEnv("GROUP_ID", "order-service-groupID")
            .withEnv("MESSAGE_TOPIC", "order-status-topic")
            .withEnv("ORDER_TOPIC", "order-topic")
            .withNetwork(NETWORK);


    @BeforeAll
    static void beforeAll() {
        kafka.start();
        orderStatusServiceContainer.start();

    }

    @AfterAll
    static void afterAll() {
        kafka.stop();
        orderStatusServiceContainer.stop();
    }

   @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    public void whenGetOrderWhenReceiveStaticMessage() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-service-groupID");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");


        KafkaConsumer<String, StatusMessage> consumer = new KafkaConsumer(props);
        consumer.subscribe(Collections.singletonList("order-status-topic"));


        OrderEvent orderEvent = new OrderEvent("Product 1", "1");
        kafkaTemplate.send("order-topic", orderEvent);

         await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    ConsumerRecords<String, StatusMessage> record = consumer.poll(Duration.ofSeconds(3));

                    Assertions.assertThat(record.count()).isEqualTo(1);
                    Assertions.assertThat(record.iterator().next().value().getStatus()).isEqualTo("CREATED");
                });
    }

}
