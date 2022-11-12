package com.shop4me.productdatastream.application.requesting.handler;

import com.shop4me.productdatastream.domain.port.requesting.handler.CategoryRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.RequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.ProductRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.ReviewRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.utils.EntityResolver;
import com.shop4me.productdatastream.domain.service.requesting.Shop4MeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor

@Configuration
public class Shop4MeCoreRequestHandlerConfig {

    private final ProductRequestHandler productRequestHandler;

    private final CategoryRequestHandler categoryRequestHandler;

    private final ReviewRequestHandler reviewRequestHandler;

    private final EntityResolver entityResolver;

    @Bean
    public RequestHandler coreRequestHandler(){
        return new Shop4MeRequestService(
                productRequestHandler,
                categoryRequestHandler,
                reviewRequestHandler,
                entityResolver
        );
    }
}
