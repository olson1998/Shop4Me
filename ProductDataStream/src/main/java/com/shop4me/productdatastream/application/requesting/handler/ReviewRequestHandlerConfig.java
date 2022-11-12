package com.shop4me.productdatastream.application.requesting.handler;

import com.shop4me.productdatastream.domain.port.persisting.review.ReviewDeletingExecutor;
import com.shop4me.productdatastream.domain.port.persisting.review.ReviewEditingExecutor;
import com.shop4me.productdatastream.domain.port.persisting.review.ReviewSavingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.handler.ReviewRequestHandler;
import com.shop4me.productdatastream.domain.service.requesting.Shop4MeReviewRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor

@Configuration
public class ReviewRequestHandlerConfig {

    private final ReviewSavingExecutor reviewSavingExecutor;

    private final ReviewEditingExecutor reviewEditingExecutor;

    private final ReviewDeletingExecutor reviewDeletingExecutor;

    @Bean
    public ReviewRequestHandler reviewRequestHandler(){
        return new Shop4MeReviewRequestService(
                reviewSavingExecutor,
                reviewEditingExecutor,
                reviewDeletingExecutor
        );
    }
}
