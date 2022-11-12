package com.shop4me.productdatastream.domain.port.requesting.handler;

import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;

public interface CategoryRequestHandler {

    Object handle(InboundMsg inboundMsg);
}
