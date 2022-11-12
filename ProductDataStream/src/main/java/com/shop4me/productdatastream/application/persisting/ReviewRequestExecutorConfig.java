package com.shop4me.productdatastream.application.persisting;

import com.shop4me.productdatastream.domain.model.dao.productdatastorage.ReviewEntity;
import com.shop4me.productdatastream.domain.port.persisting.review.ReviewDeletingExecutor;
import com.shop4me.productdatastream.domain.port.persisting.review.ReviewEditingExecutor;
import com.shop4me.productdatastream.domain.port.persisting.review.ReviewSavingExecutor;
import com.shop4me.productdatastream.domain.service.persisting.review.ReviewDeletingService;
import com.shop4me.productdatastream.domain.service.persisting.review.ReviewEditingService;
import com.shop4me.productdatastream.domain.service.persisting.review.ReviewSavingService;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;

@Configuration
public class ReviewRequestExecutorConfig {

    private final EntityManager entityManager;

    @Bean
    public ReviewSavingExecutor reviewSavingExecutor(){
        return new ReviewSavingService(entityManager);
    }

    @Bean
    public ReviewEditingExecutor reviewEditingExecutor(){
        return new ReviewEditingService(entityManager);
    }

    @Bean
    public ReviewDeletingExecutor reviewDeletingExecutor(){
        return new ReviewDeletingService(entityManager);
    }

    public ReviewRequestExecutorConfig(@NonNull JpaContext jpaContext) {
        this.entityManager = jpaContext.getEntityManagerByManagedType(ReviewEntity.class);
    }
}
