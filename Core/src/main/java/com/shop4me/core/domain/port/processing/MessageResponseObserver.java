package com.shop4me.core.domain.port.processing;

import com.shop4me.core.domain.port.dto.InboundMsg;

import java.util.concurrent.CompletableFuture;

public interface MessageResponseObserver {

    void receive(InboundMsg inboundMsg);
}
