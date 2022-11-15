package com.shop4me.core.application.repositories.productdatastream.kafka;

import com.shop4me.core.domain.port.processing.MessageResponseListenersManager;
import com.shop4me.core.domain.service.processing.MessageResponseListenersManagingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageResponseListeningManagerConfig {

    @Bean
    public MessageResponseListenersManager messageResponseListenersManager(){
        return new MessageResponseListenersManagingService();
    }
}
