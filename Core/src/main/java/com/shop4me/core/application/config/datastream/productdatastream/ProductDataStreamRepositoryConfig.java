package com.shop4me.core.application.config.datastream.productdatastream;

import com.shop4me.core.domain.port.web.datastream.ProductDataStream;
import com.shop4me.core.domain.port.web.datastream.productdatastream.CategoryRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ProductRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ReviewRepository;
import com.shop4me.core.domain.service.requesting.productdatastream.CategoryService;
import com.shop4me.core.domain.service.requesting.productdatastream.ProductService;
import com.shop4me.core.domain.service.requesting.productdatastream.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor

@Configuration
public class ProductDataStreamRepositoryConfig {

    private final ProductDataStream productDataStream;

    @Bean
    public ProductRepository productRepository(){
        return new ProductService(productDataStream);
    }

    @Bean
    public CategoryRepository categoryRepository(){
        return new CategoryService(productDataStream);
    }

    @Bean
    public ReviewRepository reviewRepository(){
        return new ReviewService(productDataStream);
    }
}
