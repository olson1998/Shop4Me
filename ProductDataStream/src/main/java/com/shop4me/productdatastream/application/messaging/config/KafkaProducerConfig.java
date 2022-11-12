package com.shop4me.productdatastream.application.messaging.config;

import com.shop4me.productdatastream.application.messaging.OutboundMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static java.util.Map.entry;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    @Value("${shop4me.kafka.bootstrap-address}")
    private String kafkaBootstrapAddress;

    @Bean
    public ProducerFactory<String, OutboundMessage> producerFactory() {
        var producerProps = producerProps();
        return new DefaultKafkaProducerFactory<>(producerProps);
    }

    @Bean
    public KafkaTemplate<String, OutboundMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    private Map<String, Object> producerProps(){
        return Map.ofEntries(
                entry(BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapAddress),
                entry(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class),
                entry(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
        );
    }
}
