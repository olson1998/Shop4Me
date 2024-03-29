package com.shop4me.core.application.outbound.kafka;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.shop4me.core.application.dto.InboundMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

import static java.util.Map.entry;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Value("${shop4me.kafka.bootstrap-address}")
    private String kafkaBootstrapAddress;

    private final StringDeserializer stringDeserializer = new StringDeserializer();
    @Bean
    public ConsumerFactory<String, InboundMessage> inboundMessageConsumerFactory(){
        var consumerProps = consumerProps();
        var jsonDeserializer = jsonDeserializer();
        return new DefaultKafkaConsumerFactory<>(
                consumerProps,
                stringDeserializer,
                jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InboundMessage> inboundMessageKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, InboundMessage>();
        factory.setConsumerFactory(inboundMessageConsumerFactory());
        return factory;
    }

    private Map<String, Object> consumerProps(){
        return Map.ofEntries(
                entry(BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapAddress),
                entry(GROUP_ID_CONFIG, "request-response"),
                entry(KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class),
                entry(VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class)
        );
    }

    private ErrorHandlingDeserializer<InboundMessage> jsonDeserializer(){
        var jsonDeserializer = new JsonDeserializer<>(InboundMessage.class);
        //jsonDeserializer.addTrustedPackages("com.shop4me.productdatastream.model.dto.*");
        jsonDeserializer.ignoreTypeHeaders();
        return new ErrorHandlingDeserializer<>(jsonDeserializer);
    }
}
