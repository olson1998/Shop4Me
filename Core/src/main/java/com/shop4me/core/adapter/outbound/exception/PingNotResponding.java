package com.shop4me.core.adapter.outbound.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class PingNotResponding extends Throwable {

    private final String url;

    private final String component;

    @Override
    public String getMessage() {
        return "pinging service " + component +" not responding on url: " + url;
    }
}
