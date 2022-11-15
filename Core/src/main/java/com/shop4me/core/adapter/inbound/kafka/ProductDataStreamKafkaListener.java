package com.shop4me.core.adapter.inbound.kafka;

import com.shop4me.core.adapter.inbound.kafka.exception.UnclaimedMessageException;
import com.shop4me.core.application.dto.InboundMessage;
import com.shop4me.core.domain.port.processing.MessageResponseListenersManager;
import com.shop4me.core.domain.port.requesting.AdminMessagingRequestRepository;
import com.shop4me.core.domain.port.requesting.MessageReceivingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.shop4me.core.domain.service.processing.AdminMessageRequestService.ADMIN_SERVICE_FINGERPRINT;

@Slf4j

@Service
@RequiredArgsConstructor
public class ProductDataStreamKafkaListener {

    private final MessageResponseListenersManager messageResponseListenersManager;

    private final AdminMessagingRequestRepository adminMessagingRequestRepository;

    @KafkaListener(topics = {
            "${shop4me.topic.inbound.product}",
            "${shop4me.topic.inbound.category}",
            "${shop4me.topic.inbound.review}",
            "${shop4me.topic.inbound.imageurl}",},
            groupId = "product-data-stream-in",
            containerFactory = "inboundMessageKafkaListenerContainerFactory")
    public void consumeInboundMessage(InboundMessage inboundMessage){
        var correlationId = inboundMessage.getMessageId();
        try{
            claimingService(correlationId).receiveResponse(inboundMessage);
        }catch (UnclaimedMessageException e){
            log.error(e.getMessage());
        }catch (NullPointerException e){
            log.error("Message without correlation id, source: 'PRODUCT_DATA_STREAM'");
        }
    }

    private MessageReceivingService claimingService(String correlationId){
        var fingerprint = messageResponseListenersManager.getServiceFingerprint(correlationId);
        if(fingerprint.equals(ADMIN_SERVICE_FINGERPRINT)){
            return adminMessagingRequestRepository;
        }else {
            throw new UnclaimedMessageException("PRODUCT_DATA_STREAM", correlationId);
        }
    }
}
