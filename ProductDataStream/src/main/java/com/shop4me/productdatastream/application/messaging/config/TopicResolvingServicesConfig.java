package com.shop4me.productdatastream.application.messaging.config;

import com.shop4me.productdatastream.domain.port.requesting.utils.EntityResolver;
import com.shop4me.productdatastream.domain.port.requesting.utils.OutboundTopicResolver;
import com.shop4me.productdatastream.domain.service.requesting.utils.OutboundTopicResolvingService;
import com.shop4me.productdatastream.domain.service.requesting.utils.Shop4MeEntityResolvingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicResolvingServicesConfig {

    @Value("${shop4me.topic.unresolved}")
    private String unresolvedEntityTopic;

    @Value("${shop4me.topic.inbound.product}")
    private String productInboundTopic;

    @Value("${shop4me.topic.inbound.category}")
    private String categoryInboundTopic;

    @Value("${shop4me.topic.inbound.review}")
    private String reviewInboundTopic;

    @Value("${shop4me.topic.inbound.imageurl}")
    private String imageUrlInboundTopic;

    @Value("${shop4me.topic.outbound.product}")
    private String productOutboundTopic;

    @Value("${shop4me.topic.outbound.category}")
    private String categoryOutboundTopic;

    @Value("${shop4me.topic.outbound.review}")
    private String reviewOutboundTopic;

    @Value("${shop4me.topic.outbound.imageurl}")
    private String imageUrlOutboundTopic;

    @Bean
    public EntityResolver entityResolver(){
        return new Shop4MeEntityResolvingService(
                productInboundTopic,
                categoryInboundTopic,
                reviewInboundTopic,
                imageUrlInboundTopic
        );
    }

    @Bean
    public OutboundTopicResolver outboundTopicResolver(){
        return new OutboundTopicResolvingService(
                unresolvedEntityTopic,
                productInboundTopic,
                categoryInboundTopic,
                reviewInboundTopic,
                imageUrlInboundTopic,
                productOutboundTopic,
                categoryOutboundTopic,
                reviewOutboundTopic,
                imageUrlOutboundTopic
        );
    }
}
