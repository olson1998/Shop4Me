package com.shop4me.core.domain.model.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class WebClientNotSetUpException extends IllegalStateException {

    private final String shop4MeComponent;

    @Override
    public String getMessage() {
        return shop4MeComponent + " is not ready...";
    }
}
