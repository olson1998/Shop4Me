package com.shop4me.productdatastream.application.requesting.handler;

import com.shop4me.productdatastream.domain.port.persisting.category.CategoryObtainExecutor;
import com.shop4me.productdatastream.domain.port.persisting.category.CategorySaveExecutor;
import com.shop4me.productdatastream.domain.port.requesting.handler.CategoryRequestHandler;
import com.shop4me.productdatastream.domain.service.requesting.Shop4MeCategoryRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor

@Configuration
public class CategoryRequestHandlerConfig {

    private final CategoryObtainExecutor categoryObtainExecutor;

    private final CategorySaveExecutor categorySaveExecutor;

    @Bean
    public CategoryRequestHandler categoryRequestHandler(){
        return new Shop4MeCategoryRequestService(
                categoryObtainExecutor,
                categorySaveExecutor
        );
    }
}
