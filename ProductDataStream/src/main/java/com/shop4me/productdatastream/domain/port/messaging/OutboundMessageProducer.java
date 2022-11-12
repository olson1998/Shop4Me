package com.shop4me.productdatastream.domain.port.messaging;

public interface OutboundMessageProducer {

    void produce(InboundMsg inboundMsg, String status, String header, String payload);

    void produce(InboundMsg inboundMsg, String status, String payload);
}
