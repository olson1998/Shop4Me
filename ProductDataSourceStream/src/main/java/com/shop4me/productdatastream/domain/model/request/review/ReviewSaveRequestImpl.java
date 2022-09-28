package com.shop4me.productdatastream.domain.model.request.review;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shop4me.productdatastream.domain.model.data.dto.Review;
import com.shop4me.productdatastream.domain.model.request.toolset.RequestPayloadReader;
import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ReviewDto;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.ReviewSaveRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor

public class ReviewSaveRequestImpl implements ReviewSaveRequest {

    @Getter
    private final Map<String, ReviewDto> requestedToSave;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("SAVE REVIEW: (");
        List<String> reviewsCorrelationIdList = new java.util.ArrayList<>(List.copyOf(requestedToSave.keySet()));
        reviewsCorrelationIdList.sort(Comparator.naturalOrder());
        int len = reviewsCorrelationIdList.size();
        int last = len-1;
        for(int i = 0; i < len; i++){
           var correlationId = reviewsCorrelationIdList.get(i);
            var review = requestedToSave.get(correlationId);
            str.append("(REVIEWER ID: '").append(review.getReviewerId()).append("' ");
            str.append("PRODUCT ID: '").append(review.getReviewerId()).append("')");
            if(i < last){
                str.append(", ");
            }
        }
        str.append(")");
        return str.toString();
    }

    @SneakyThrows
    public static ReviewSaveRequestImpl fromCoreRequest(CoreRequest request){
        var json = request.decodePayload().getPayload();

        EmptyPayloadCheck.scan(json, "{}");

        var reviewMap = RequestPayloadReader.OBJECT_MAPPER
                .readValue(json, new TypeReference<Map<String, Review>>() {});

        var reviewDtoMap = writeReviewDtoMap(reviewMap);

        return new ReviewSaveRequestImpl(reviewDtoMap);
    }

    private static Map<String, ReviewDto> writeReviewDtoMap(Map<String, Review> reviewMap){
        var reviewDtoMap = new HashMap<String, ReviewDto>();

        reviewMap.keySet().forEach(correlationId -> {
            var review = reviewMap.get(correlationId);
            reviewDtoMap.put(correlationId, review);
        });
        return reviewDtoMap;
    }
}
