package com.shop4me.core.adapter.inbound.kafka.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnclaimedMessageException extends IllegalStateException{

    private final String component;

    private final String correlationId;

    @Override
    public String getMessage() {
        return String.format("Unclaimed message: '%s', source: '%s'", correlationId, component);
    }
}
