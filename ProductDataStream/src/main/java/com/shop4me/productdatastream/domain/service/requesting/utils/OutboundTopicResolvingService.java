package com.shop4me.productdatastream.domain.service.requesting.utils;

import com.shop4me.productdatastream.domain.port.requesting.utils.OutboundTopicResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutboundTopicResolvingService implements OutboundTopicResolver {

    private final String unresolvedEntityTopic;

    private final String productInboundTopic;

    private final String categoryInboundTopic;

    private final String reviewInboundTopic;

    private final String imageUrlInboundTopic;

    private final String productOutboundTopic;

    private final String categoryOutboundTopic;

    private final String reviewOutboundTopic;

    private final String imageUrlOutboundTopic;

    @Override
    public String resolve(@NonNull String topic) {
        if(topic.equals(productInboundTopic)){
            return productOutboundTopic;
        } else if (topic.equals(categoryInboundTopic)) {
            return categoryOutboundTopic;
        } else if (topic.equals(reviewInboundTopic)) {
            return reviewOutboundTopic;
        }else if(topic.equals(imageUrlInboundTopic)){
            return imageUrlOutboundTopic;
        }else {
            return unresolvedEntityTopic;
        }
    }
}
