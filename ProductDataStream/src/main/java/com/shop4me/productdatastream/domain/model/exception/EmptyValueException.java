package com.shop4me.productdatastream.domain.model.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

public class EmptyValueException extends IllegalArgumentException{

    private String msg = "empty value";

    @Override
    public String getMessage() {
        return "reason: " + msg;
    }
}
