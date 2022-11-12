package com.shop4me.productdatastream.adapter.inbound;

import com.shop4me.productdatastream.application.messaging.InboundMessage;
import com.shop4me.productdatastream.domain.port.requesting.handler.InboundMessageProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_TOPIC;

@Service

@EnableKafka
@RequiredArgsConstructor
public class InboundMessageConsumer {

    private final InboundMessageProcessor inboundMessageProcessor;

    @KafkaListener(topics = {
            "${shop4me.topic.inbound.product}",
            "${shop4me.topic.inbound.category}",
            "${shop4me.topic.inbound.review}",
            "${shop4me.topic.inbound.imageurl}",},
            groupId = "inbound-req",
            containerFactory = "inboundMessageKafkaListenerContainerFactory")
    public void consumeInboundMessage(@Header(RECEIVED_TOPIC) String topic, InboundMessage inboundMsg){
        inboundMsg.setTopic(topic);
        inboundMessageProcessor.process(inboundMsg);
    }
}
