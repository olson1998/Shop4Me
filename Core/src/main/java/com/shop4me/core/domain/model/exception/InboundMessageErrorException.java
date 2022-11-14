package com.shop4me.core.domain.model.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InboundMessageErrorException extends Throwable {

    private final String header;

    private final String payload;

    @Override
    public String getMessage() {
        return "'"+header+"': " + payload;
    }
}
