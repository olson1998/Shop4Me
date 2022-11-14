package com.shop4me.core.domain.port.requesting;

import com.shop4me.core.application.dto.productdatastream.Review;
import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CustomerRequestRepository {

    CompletableFuture<RequestProcessingReport> saveCustomerReview(int tenantId, List<ReviewDto> reviews);

    CompletableFuture<RequestProcessingReport> editCustomerReview(int tenantId, Map<String, String> reviewPropertyNewValueMap);

    CompletableFuture<RequestProcessingReport> deleteCustomerReview(int tenantId, Review review);
}

