package com.shop4me.core.application.repositories.config.datastream.productdatastream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ProductTopicListenerRepository;
import com.shop4me.core.domain.port.web.messaging.MessageProducerRepository;
import com.shop4me.core.domain.service.requesting.productdatastream.ProductTopicListenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MessagingOutboundRepositoriesConfig {

    @Value("${shop4me.topic.outbound.product}")
    private String productOutboundTopic;

    private final ObjectMapper objectMapper;

    private final MessageProducerRepository messageProducerRepository;

    @Bean
    public ProductTopicListenerRepository productTopicListenerRepository(){
        return new ProductTopicListenerService(
                productOutboundTopic,
                objectMapper,
                messageProducerRepository
        );
    }
}
