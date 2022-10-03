package com.shop4me.core.domain.port.requesting;

import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CustomerRequestRepository {

    CompletableFuture<Map<String, String>> saveCustomerReview(List<ReviewDto> reviews);
}

