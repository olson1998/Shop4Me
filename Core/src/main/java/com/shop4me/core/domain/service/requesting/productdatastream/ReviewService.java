package com.shop4me.core.domain.service.requesting.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import com.shop4me.core.domain.port.web.datastream.ProductDataStream;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public class ReviewService implements ReviewRepository {

    private final ProductDataStream productDataStream;

    @Override
    public Mono<Map<String, String>> saveReviews(List<ReviewDto> reviews) {
        var reviewSaveMap = createReviewSaveMap(reviews);
        return productDataStream.requestSavingReview(reviewSaveMap);
    }

    @Override
    public Mono<Map<String, Integer>> editReview(Map<String, String> reviewEditMap) {
        return productDataStream.requestEditingReview(reviewEditMap);
    }

    @Override
    public Mono<Map<String, Integer>> deleteReview(ReviewDto review) {
        return productDataStream.requestDeletingReview(review);
    }

    private Map<String, ReviewDto> createReviewSaveMap(@NonNull List<ReviewDto> reviews){
        var reviewSaveMap = new ConcurrentHashMap<String, ReviewDto>();
        reviews.forEach(reviewDto -> reviewSaveMap.put(UUID.randomUUID().toString(), reviewDto));
        return reviewSaveMap;
    }

}
