package com.shop4me.core.domain.model.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductIdNotANumberException extends IllegalArgumentException{

    private final String givenId;

    @Override
    public String getMessage() {
        return "Product id should be a number... Got: '" + givenId + "'";
    }
}
