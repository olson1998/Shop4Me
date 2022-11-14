package com.shop4me.productdatastream.domain.port.requesting;

import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;

public interface CoreRequest {

    int getTenantId();

    String getEntity();

    String getOperation();

    String getPayload();

    InboundMsg toInboundMessage();
}
