package com.shop4me.productdatastream.domain.service.requesting.utils;

import com.shop4me.productdatastream.domain.model.request.enumset.Entity;
import com.shop4me.productdatastream.domain.port.requesting.utils.EntityResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Shop4MeEntityResolvingService implements EntityResolver {

    private final String productInboundTopic;

    private final String categoryInboundTopic;

    private final String reviewInboundTopic;

    private final String imageUrlInboundTopic;

    @Override
    public String resolve(@NonNull String topic) {
        if(topic.equals(productInboundTopic)){
            return Entity.PRODUCT.name();
        } else if (topic.equals(categoryInboundTopic)) {
            return Entity.CATEGORY.name();
        }else if (topic.equals(reviewInboundTopic)){
            return Entity.REVIEW.name();
        } else if (topic.equals(imageUrlInboundTopic)) {
            return Entity.IMAGE_URL.name();
        }else {
            throw new IllegalArgumentException();
        }
    }
}
