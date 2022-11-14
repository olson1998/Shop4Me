package com.shop4me.core.application.repositories.config.datastream.productdatastream;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class RestInboundRepositoriesConfig {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ReviewRepository reviewRepository;

    private final ObjectMapper mapper;

    @Bean
    public AdminRequestRepository restApiAdminRepository(){
        return new AdminRequestService(
                mapper,
                productRepository,
                categoryRepository
        );
    }

    @Bean
    public CustomerRequestRepository customerRequestRepository(){
        return new CustomerRequestService(reviewRepository);
    }
}
