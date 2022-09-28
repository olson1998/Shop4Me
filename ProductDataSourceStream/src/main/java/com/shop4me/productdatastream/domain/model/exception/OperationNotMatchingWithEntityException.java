package com.shop4me.productdatastream.domain.model.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OperationNotMatchingWithEntityException extends IllegalArgumentException{

    private final String entity;

    private final String operation;

    @Override
    public String getMessage() {
        return "reason: entity: " + entity + " not match with: " + operation;
    }
}
