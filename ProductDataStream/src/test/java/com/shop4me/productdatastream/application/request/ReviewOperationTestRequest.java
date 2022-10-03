package com.shop4me.productdatastream.application.request;

import com.shop4me.productdatastream.application.PayloadWriter;
import com.shop4me.productdatastream.application.requesting.Shop4MeCoreRequest;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ReviewDto;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;

import java.util.Map;

import static com.shop4me.productdatastream.application.request.utils.OperationStrings.*;

public class ReviewOperationTestRequest {

    public static final String REVIEW = "REVIEW";

    public static CoreRequest reviewSaveRequest(Map<String, ReviewDto> reviewSaveMap){
        return new Shop4MeCoreRequest(
                REVIEW,
                SAVE,
                PayloadWriter.write(reviewSaveMap)
        );
    }

    public static CoreRequest reviewDeleteRequest(ReviewDto review){
        return new Shop4MeCoreRequest(
                REVIEW,
                DELETE,
                PayloadWriter.write(review)
        );
    }

    public static CoreRequest reviewDeleteRequest(Map<String, String> reviewPropertyValueMap){
        return new Shop4MeCoreRequest(
                REVIEW,
                EDIT,
                PayloadWriter.write(reviewPropertyValueMap)
        );
    }
}
