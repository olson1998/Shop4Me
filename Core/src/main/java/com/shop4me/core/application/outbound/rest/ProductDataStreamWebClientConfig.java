package com.shop4me.core.application.outbound.rest;

import com.shop4me.core.domain.port.web.client.DataStreamWebClient;
import com.shop4me.core.domain.port.web.client.DataStreamWebClientFactory;
import com.shop4me.core.domain.service.rest.DataStreamWebClientImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Slf4j

@RequiredArgsConstructor

@Configuration
public class ProductDataStreamWebClientConfig {

    private final DataStreamWebClientFactory dataStreamWebClientFactory;

    @Value("${shop4me.product-data-stream.auth.username}")
    private String username;

    @Value("${shop4me.product-data-stream.auth.password}")
    private String password;

    @Value("${shop4me.product-data-stream.url}")
    private String url;

    @Bean
    public DataStreamWebClient productDataStreamWebClient(){
        String component = "PRODUCT_DATA_STREAM";
        return new DataStreamWebClientImpl(
                dataStreamWebClientFactory,
                component,
                url,
                username,
                password
        );
    }

    @EventListener(ApplicationStartedEvent.class)
    public void establishRestServiceForProductDataStream(){
        productDataStreamWebClient().setUp();
    }
}
