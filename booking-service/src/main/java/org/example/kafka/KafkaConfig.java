package org.example.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.model.kafka.BookingMessage;
import org.example.model.kafka.RegMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapKafkaServers;
    @Value(value = "${spring.kafka.kafkaMessageGroupId}")
    private String kafkaMessageGroupId;

    public Map<String, Object> producerConfig() {

        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapKafkaServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return config;
    }

    @Bean
    public ProducerFactory<String, RegMessage> kafkaRegMessageProducerFactory() {

        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public ProducerFactory<String, BookingMessage> kafkaBookingMessageProducerFactory() {

        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean(name = "regMessage")
    public KafkaTemplate<String, RegMessage> regMessageKafkaTemplate() {

        return new KafkaTemplate<String, RegMessage>(kafkaRegMessageProducerFactory());
    }

    @Bean(name = "bookingMessage")
    public KafkaTemplate<String, BookingMessage> bookingMessageKafkaTemplate() {

        return new KafkaTemplate<String, BookingMessage>(kafkaBookingMessageProducerFactory());
    }

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    @Bean
    public Map<String, Object> consumerConfig() {

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapKafkaServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaMessageGroupId);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return config;
    }

    @Bean
    public ConsumerFactory<String, RegMessage> regMessageConsumerFactory() {

        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(objectMapper()));
    }

    @Bean
    public ConsumerFactory<String, BookingMessage> bookingMessageConsumerFactory() {

        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(objectMapper()));
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, RegMessage>
    regMessageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RegMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(regMessageConsumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, BookingMessage>
    bookingMessageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BookingMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookingMessageConsumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

}
