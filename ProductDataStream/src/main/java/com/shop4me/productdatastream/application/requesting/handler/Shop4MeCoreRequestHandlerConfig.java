package com.shop4me.productdatastream.application.requesting.handler;

import com.shop4me.productdatastream.domain.port.requesting.handler.CategoryRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.CoreRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.ProductRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.ReviewRequestHandler;
import com.shop4me.productdatastream.domain.service.requesting.Shop4MeCoreRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor

@Configuration
public class Shop4MeCoreRequestHandlerConfig {

    private final ProductRequestHandler productRequestHandler;

    private final CategoryRequestHandler categoryRequestHandler;

    private final ReviewRequestHandler reviewRequestHandler;

    @Bean
    public CoreRequestHandler coreRequestHandler(){
        return new Shop4MeCoreRequestService(
                productRequestHandler,
                categoryRequestHandler,
                reviewRequestHandler
        );
    }
}
