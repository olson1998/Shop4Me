package com.shop4me.core.domain.service.processing;

import com.shop4me.core.application.dto.product_data_stream.Review;
import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import com.shop4me.core.domain.port.requesting.CustomerRequestRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ReviewRepository;
import com.shop4me.core.domain.service.processing.report.AffectedRowsReportingServing;
import com.shop4me.core.domain.service.processing.report.ErrorReportingService;
import com.shop4me.core.domain.service.processing.report.SavingReportingService;
import com.shop4me.core.domain.service.processing.utils.RequestProcessingExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j

@AllArgsConstructor
public class CustomerRequestService implements CustomerRequestRepository {

    private final ReviewRepository reviewRepository;

    @Override
    public CompletableFuture<RequestProcessingReport> saveCustomerReview(List<ReviewDto> reviews){
        log.info("CUSTOMER ip: {}, request to save: {}", "?", reviews);
        var reviewSaveMap = createReviewSaveMap(reviews);
        return reviewRepository.saveReviews(reviewSaveMap)
                .toFuture()
                .thenApply(response -> SavingReportingService.reviewSavingReport(reviewSaveMap, response))
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editCustomerReview(Map<String, String> reviewPropertyNewValueMap){
        log.info("CUSTOMER ip: {}, requested to edit: {}", "?", reviewPropertyNewValueMap);
        return reviewRepository.editReview(reviewPropertyNewValueMap)
                .toFuture()
                .thenApply(AffectedRowsReportingServing::affectedRowsReport)
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> deleteCustomerReview(Review review){
        log.info("CUSTOMER ip: {}, requested to delete: {}", "?", review);
        return reviewRepository.deleteReview(review)
                .toFuture()
                .thenApply(AffectedRowsReportingServing::affectedRowsReport)
                .exceptionally(ErrorReportingService::error);
    }

    private Map<String, ReviewDto> createReviewSaveMap(@NonNull List<ReviewDto> reviews){
        var reviewSaveMap = new ConcurrentHashMap<String, ReviewDto>();
        reviews.forEach(reviewDto -> reviewSaveMap.put(UUID.randomUUID().toString(), reviewDto));
        return reviewSaveMap;
    }
}
