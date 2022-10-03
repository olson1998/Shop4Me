package com.shop4me.core.adapter.outbound.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class PingFalseResponse extends IllegalStateException{

    private final String component;

    private final String response;

    @Override
    public String getMessage() {
        return "False response to liveness probe of component: " + component +
                " got response: '" + response + "'";
    }
}
