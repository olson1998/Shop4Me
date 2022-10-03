package com.shop4me.core.domain.service.processing;

import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import com.shop4me.core.domain.port.requesting.CustomerRequestRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ReviewRepository;
import com.shop4me.core.domain.service.processing.utils.RequestProcessingExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j

@AllArgsConstructor
public class CustomerRequestService implements CustomerRequestRepository {

    private final ReviewRepository reviewRepository;

    @Override
    public CompletableFuture<Map<String, String>> saveCustomerReview(List<ReviewDto> reviews){
        log.info("CUSTOMER ip: {}, request to save: {}", "?", reviews);
        return reviewRepository.saveReviews(reviews)
                .toFuture()
                .exceptionally(RequestProcessingExceptionHandler::returnEmptyMap);
    }

}
