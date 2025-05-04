package com.transaction.management.configuration;


import com.transaction.management.response.NotificationResponse;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.*;

@Slf4j
@Configuration
@EnableKafka
@RequiredArgsConstructor
public class NotificationServiceProducerConfig {

    @Autowired
    private MeterRegistry meterRegistry;

    @Value("${kafka.transaction.servers}")
    private String kafkaBootstrapServers;

    @Value("${kafka.transaction.producerAck}")
    private String producerAck;

    @Value("${kafka.transaction.batchSize}")
    private String batchSize;

    @Value("${kafka.transaction.lingerMs}")
    private String lingerMs;

    @Value("${kafka.transaction.maxRequestSize}")
    private String maxRequestSize;

    @Value("${kafka.transaction.bufferMemory}")
    private String bufferMemory;

    @Value("${kafka.transaction.maxBlockMs}")
    private String maxBlockMs;

    @Value("${kafka.transaction.compression_type}")
    private String compressionType;

    @Value("${kafka.transaction.requests_per_connection}")
    private String requestsPerConnection;

    @Value("${kafka.transaction.max_age_ms}")
    private String maxAgeMs;

    @Value("${kafka.transaction.max_idle_ms}")
    private String maxIdleMs;


    // Producer Configuration
    private Map<String, Object> notificationProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, producerAck);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxRequestSize);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, maxBlockMs);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, requestsPerConnection);
        props.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, maxAgeMs);
        props.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, maxIdleMs);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false); // Optional: cleaner JSON without extra headers
        return props;
    }

    @Bean
    public KafkaTemplate<String, NotificationResponse> notificationKafkaTemplate() {
        return new KafkaTemplate<>(producerFactoryForNotification());
    }

    @Bean
    public ProducerFactory<String, NotificationResponse> producerFactoryForNotification() {
        return new DefaultKafkaProducerFactory<>(notificationProducerConfigs());
    }
}