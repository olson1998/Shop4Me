package com.shop4me.productdatastream.domain.port.requesting.handler;

import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;

import java.util.concurrent.CompletableFuture;

public interface RequestHandler {

    Object handle(InboundMsg inboundMsg);

    CompletableFuture<Object> handle(CoreRequest request);
}
