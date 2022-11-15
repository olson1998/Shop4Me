package com.shop4me.core.application.repositories.productdatastream.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.port.web.messaging.CategoryTopicListenerRepository;
import com.shop4me.core.domain.port.web.messaging.ProductTopicListenerRepository;
import com.shop4me.core.domain.port.web.messaging.MessageProducerRepository;
import com.shop4me.core.domain.service.requesting.productdatastream.message.CategoryTopicListenerService;
import com.shop4me.core.domain.service.requesting.productdatastream.message.ProductTopicListenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MessagingOutboundRepositoriesConfig {

    @Value("${shop4me.topic.outbound.product}")
    private String productOutboundTopic;

    @Value("${shop4me.topic.outbound.category}")
    private String categoryOutboundTopic;

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

    @Bean
    public CategoryTopicListenerRepository categoryTopicListenerRepository(){
        return new CategoryTopicListenerService(
                categoryOutboundTopic,
                objectMapper,
                messageProducerRepository
        );
    }
}
