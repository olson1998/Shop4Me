package com.shop4me.core.adapter.inbound.messaging;

import com.shop4me.core.application.dto.InboundMessage;
import com.shop4me.core.domain.port.requesting.AdminMessageRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDataStreamKafkaListener {

    private final AdminMessageRequestRepository adminMessageRequestRepository;

    @KafkaListener(topics = {
            "${shop4me.topic.inbound.product}",
            "${shop4me.topic.inbound.category}",
            "${shop4me.topic.inbound.review}",
            "${shop4me.topic.inbound.imageurl}",},
            groupId = "inbound-req",
            containerFactory = "inboundMessageKafkaListenerContainerFactory")
    public void consumeInboundMessage(InboundMessage inboundMessage){
        adminMessageRequestRepository.receiveResponse(inboundMessage);
    }
}
