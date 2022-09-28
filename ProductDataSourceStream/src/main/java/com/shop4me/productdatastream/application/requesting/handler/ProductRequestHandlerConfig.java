package com.shop4me.productdatastream.application.requesting.handler;

import com.shop4me.productdatastream.domain.port.persisting.repositories.product.*;
import com.shop4me.productdatastream.domain.port.requesting.handler.ProductRequestHandler;
import com.shop4me.productdatastream.domain.service.requesting.Shop4MeProductRequestService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor

@Configuration
public class ProductRequestHandlerConfig {

    private final ProductObtainingExecutor productObtainingExecutor;

    private final ProductSavingExecutor productSavingExecutor;

    private final ProductSearchingExecutor productSearchingExecutor;

    private final ProductEditingExecutor productEditingExecutor;

    private final ProductDeletingExecutor productDeletingExecutor;

    @Bean
    public ProductRequestHandler productRequestHandler(){
        return new Shop4MeProductRequestService(
                productObtainingExecutor,
                productSearchingExecutor,
                productSavingExecutor,
                productEditingExecutor,
                productDeletingExecutor
        );
    }
}
