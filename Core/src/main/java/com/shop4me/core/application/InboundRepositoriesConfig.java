package com.shop4me.core.application;

import com.shop4me.core.domain.port.requesting.AdminRequestRepository;
import com.shop4me.core.domain.port.requesting.CustomerRequestRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.CategoryRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ProductRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ReviewRepository;
import com.shop4me.core.domain.service.processing.AdminRequestService;
import com.shop4me.core.domain.service.processing.CustomerRequestService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor

@Configuration
public class InboundRepositoriesConfig {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ReviewRepository reviewRepository;

    @Bean
    public AdminRequestRepository adminRequestRepository(){
        return new AdminRequestService(
                productRepository,
                categoryRepository
        );
    }

    @Bean
    public CustomerRequestRepository customerRequestRepository(){
        return new CustomerRequestService(reviewRepository);
    }
}
