package com.shop4me.core.domain.port.web.datastream.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ReviewRepository {

    Mono<Map<String, String>> saveReviews(int tenantId, Map<String, ReviewDto> reviews);

    Mono<Map<String, Integer>> editReview(int tenantId, Map<String, String> reviewEditMap);

    Mono<Map<String, Integer>> deleteReview(int tenantId, ReviewDto review);
}
