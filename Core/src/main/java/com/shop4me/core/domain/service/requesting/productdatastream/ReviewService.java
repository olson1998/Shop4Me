package com.shop4me.core.domain.service.requesting.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import com.shop4me.core.domain.port.web.datastream.ProductDataStream;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ReviewRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

@AllArgsConstructor
public class ReviewService implements ReviewRepository {

    private final ProductDataStream productDataStream;

    @Override
    public Mono<Map<String, String>> saveReviews(int tenantId, Map<String, ReviewDto> reviewSaveMap) {
        return productDataStream.requestSavingReview(tenantId, reviewSaveMap);
    }

    @Override
    public Mono<Map<String, Integer>> editReview(int tenantId, Map<String, String> reviewEditMap) {
        return productDataStream.requestEditingReview(tenantId, reviewEditMap);
    }

    @Override
    public Mono<Map<String, Integer>> deleteReview(int tenantId, ReviewDto review) {
        return productDataStream.requestDeletingReview(tenantId, review);
    }

}
