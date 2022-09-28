package com.shop4me.productdatastream.domain.model.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor

public class Shop4MeCoreRequestExecutionException extends Throwable {

    private final String entity;

    private final String operation;

    private final Throwable reason;

    @Override
    public String getMessage() {
        return "Could not execute request: " + entity + " " + operation +
                ", reason: [" + reason.getMessage() + "]";
    }
}
