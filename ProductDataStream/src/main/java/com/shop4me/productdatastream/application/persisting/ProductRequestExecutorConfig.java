package com.shop4me.productdatastream.application.persisting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.port.persisting.product.*;
import com.shop4me.productdatastream.domain.service.persisting.product.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;

@Configuration
public class ProductRequestExecutorConfig {

    private final EntityManager entityManager;

    @Bean
    public ProductSavingExecutor productSavingRepository(){
        return new ProductSavingService(entityManager);
    }

    @Bean
    public ProductSearchingExecutor productSearchingExecutor(){
        return new ProductSearchingService(entityManager);
    }

    @Bean
    public ProductObtainingExecutor productObtainingExecutor(){
        return new ProductObtainingService(entityManager);
    }

    @Bean
    public ProductDeletingExecutor productDeletingExecutor(){
        return new ProductDeletingService(entityManager);
    }

    @Bean
    public ProductEditingExecutor productEditingExecutor(ObjectMapper mapper){
        return new ProductEditingService(entityManager, mapper);
    }

    public ProductRequestExecutorConfig(@NotNull JpaContext jpaContext) {
        this.entityManager = jpaContext.getEntityManagerByManagedType(ProductEntity.class);
    }
}
