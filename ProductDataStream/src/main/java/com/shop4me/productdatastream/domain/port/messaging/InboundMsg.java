package com.shop4me.productdatastream.domain.port.messaging;

public interface InboundMsg {

    String getMessageId();

    String getTopic();
    int getTenantId();

    String getOperation();

    String getPayload();

    void setTopic(String topic);

    String getDecodedPayload();
}
