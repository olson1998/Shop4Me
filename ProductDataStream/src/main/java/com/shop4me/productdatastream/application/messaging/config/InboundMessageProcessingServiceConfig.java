package com.shop4me.productdatastream.application.messaging.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.port.messaging.OutboundMessageProducer;
import com.shop4me.productdatastream.domain.port.requesting.handler.RequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.InboundMessageProcessor;
import com.shop4me.productdatastream.domain.port.requesting.utils.EntityResolver;
import com.shop4me.productdatastream.domain.service.requesting.InboundMessageProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InboundMessageProcessingServiceConfig {

    private final OutboundMessageProducer outboundMessageProducer;

    private final RequestHandler requestHandler;

    private final ObjectMapper objectMapper;

    @Bean
    public InboundMessageProcessor inboundMessageProcessor(){
        return new InboundMessageProcessingService(
                objectMapper,
                requestHandler,
                outboundMessageProducer
        );
    }
}
