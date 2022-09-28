package com.shop4me.productdatastream.application.persisting;

import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.port.persisting.repositories.category.CategoryObtainExecutor;
import com.shop4me.productdatastream.domain.port.persisting.repositories.category.CategorySaveExecutor;
import com.shop4me.productdatastream.domain.service.persisting.category.CategoryObtainService;
import com.shop4me.productdatastream.domain.service.persisting.category.CategorySaveService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;

@Configuration
public class CategoryRequestExecutorConfig {

    private final EntityManager entityManager;

    @Bean
    public CategoryObtainExecutor categoryObtainExecutor(){
        return new CategoryObtainService(entityManager);
    }

    @Bean
    public CategorySaveExecutor categorySaveExecutor(){
        return new CategorySaveService(entityManager);
    }

    public CategoryRequestExecutorConfig(@NotNull JpaContext jpaContext) {
        this.entityManager = jpaContext.getEntityManagerByManagedType(CategoryEntity.class);
    }
}
