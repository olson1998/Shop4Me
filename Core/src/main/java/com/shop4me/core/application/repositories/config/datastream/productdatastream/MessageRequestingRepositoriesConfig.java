package com.shop4me.core.application.repositories.config.datastream.productdatastream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.port.requesting.AdminRequestRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ProductTopicListenerRepository;
import com.shop4me.core.domain.service.processing.AdminMessageRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MessageRequestingRepositoriesConfig {

    private final ObjectMapper objectMapper;

    private final ProductTopicListenerRepository productTopicListenerRepository;

    @Bean
    public AdminRequestRepository messagingRequestAdminRepository(){
        return new AdminMessageRequestService(objectMapper, productTopicListenerRepository);
    }
}
