package com.shop4me.core.application.repositories.productdatastream.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.port.processing.MessageResponseListenersManager;
import com.shop4me.core.domain.port.requesting.AdminMessagingRequestRepository;
import com.shop4me.core.domain.port.web.messaging.CategoryTopicListenerRepository;
import com.shop4me.core.domain.port.web.messaging.ProductTopicListenerRepository;
import com.shop4me.core.domain.service.processing.AdminMessageRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MessageRequestingRepositoriesConfig {

    private final ObjectMapper objectMapper;

    private final ProductTopicListenerRepository productTopicListenerRepository;

    private final CategoryTopicListenerRepository categoryTopicListenerRepository;

    private final MessageResponseListenersManager messageResponseListenersManager;

    @Bean
    public AdminMessagingRequestRepository adminMessagingRequestRepository(){
        return new AdminMessageRequestService(
                objectMapper,
                productTopicListenerRepository,
                categoryTopicListenerRepository,
                messageResponseListenersManager
        );
    }
}
