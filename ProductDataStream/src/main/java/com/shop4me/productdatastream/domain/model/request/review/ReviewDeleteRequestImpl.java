package com.shop4me.productdatastream.domain.model.request.review;

import com.shop4me.productdatastream.domain.model.request.utils.RequestPayloadReader;
import com.shop4me.productdatastream.domain.model.dto.Review;
import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.requesting.ReviewDeleteRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.VisibleForTesting;

@RequiredArgsConstructor

public class ReviewDeleteRequestImpl implements ReviewDeleteRequest {

    @VisibleForTesting
    private final Review reviewToDelete;

    public String writeJpqlQuery(){
        return  "delete from ReviewEntity r where r.id= "+ reviewToDelete.getId() +
                " and r.reviewerId= " + reviewToDelete.getReviewerId() +
                " and r.productId= " + reviewToDelete.getProductId();
    }

    @Override
    public String toString() {
        return "DELETE REVIEW WHERE: (ID LIKE: '"+ reviewToDelete.getId() + "', " +
                "REVIEWER ID LIKE: '" + reviewToDelete.getReviewerId() +"', " +
                "PRODUCT ID LIKE: '" + reviewToDelete.getProductId() + "')";
    }

    @SneakyThrows
    public static ReviewDeleteRequestImpl fromInboundMessage(@NonNull InboundMsg inboundMsg){
        var json = inboundMsg.getDecodedPayload();

        EmptyPayloadCheck.scan(json, "{}");

        var toDelete= RequestPayloadReader.OBJECT_MAPPER.readValue(json, Review.class);
        return new ReviewDeleteRequestImpl(toDelete);
    }
}
