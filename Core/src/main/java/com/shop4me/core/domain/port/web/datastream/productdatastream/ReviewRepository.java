package com.shop4me.core.domain.port.web.datastream.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface ReviewRepository {

    Mono<Map<String, String>> saveReviews(List<ReviewDto> reviews);

    Mono<Map<String, Integer>> editReview(Map<String, String> reviewEditMap);

    Mono<Map<String, Integer>> deleteReview(ReviewDto review);
}
