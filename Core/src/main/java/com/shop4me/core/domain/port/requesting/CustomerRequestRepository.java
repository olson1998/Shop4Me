package com.shop4me.core.domain.port.requesting;

import com.shop4me.core.application.dto.product_data_stream.Review;
import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CustomerRequestRepository {

    CompletableFuture<RequestProcessingReport> saveCustomerReview(List<ReviewDto> reviews);

    CompletableFuture<RequestProcessingReport> editCustomerReview(Map<String, String> reviewPropertyNewValueMap);

    CompletableFuture<RequestProcessingReport> deleteCustomerReview(Review review);
}

