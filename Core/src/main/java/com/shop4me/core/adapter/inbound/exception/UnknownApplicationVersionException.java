package com.shop4me.core.adapter.inbound.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnknownApplicationVersionException extends IllegalArgumentException {

    private final String givenVersion;

    @Override
    public String getMessage() {
        return "unknown application version: " + givenVersion;
    }
}
