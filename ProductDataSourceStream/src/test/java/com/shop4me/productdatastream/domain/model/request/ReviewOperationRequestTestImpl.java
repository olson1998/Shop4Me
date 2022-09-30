package com.shop4me.productdatastream.domain.model.request;

import com.shop4me.productdatastream.domain.model.data.dto.Review;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.review.ReviewProperty;
import com.shop4me.productdatastream.domain.model.request.review.ReviewDeleteRequestImpl;
import com.shop4me.productdatastream.domain.model.request.review.ReviewEditRequestImpl;
import com.shop4me.productdatastream.domain.model.request.review.ReviewSaveRequestImpl;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ReviewDto;
import com.shop4me.productdatastream.domain.port.requesting.ReviewDeleteRequest;
import com.shop4me.productdatastream.domain.port.requesting.ReviewEditRequest;
import com.shop4me.productdatastream.domain.port.requesting.ReviewSaveRequest;

import java.util.Map;

public class ReviewOperationRequestTestImpl {

    public static ReviewSaveRequest reviewSaveRequest(Map<String, ReviewDto> reviewSaveMap){
        return new ReviewSaveRequestImpl(reviewSaveMap);
    }

    public static ReviewDeleteRequest reviewDeleteRequest(Review review){
        return new ReviewDeleteRequestImpl(review);
    }

    public static ReviewEditRequest reviewEditRequest(Map<ReviewProperty, String> reviewEditPropertyMap){
        return new ReviewEditRequestImpl(reviewEditPropertyMap);
    }
}
