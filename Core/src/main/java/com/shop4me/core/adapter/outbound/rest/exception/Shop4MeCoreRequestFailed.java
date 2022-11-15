package com.shop4me.core.adapter.outbound.rest.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor

public class Shop4MeCoreRequestFailed extends IllegalArgumentException{

    private final String cause;

    @Override
    public String getMessage() {
        return "Reason: " + cause;
    }
}
