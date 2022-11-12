package com.shop4me.productdatastream.application.messaging;

import com.shop4me.productdatastream.domain.model.request.enumset.Operation;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;

import java.util.UUID;

public class InboundMessageGenerator {

    public static InboundMsg generate(String topic, int tenantId, Operation operation, String payload){
        return new InboundMessage(
                UUID.randomUUID().toString(),
                topic,
                tenantId,
                operation.name(),
                payload
        );
    }
}
