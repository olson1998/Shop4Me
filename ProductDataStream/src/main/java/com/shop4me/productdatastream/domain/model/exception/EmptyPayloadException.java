package com.shop4me.productdatastream.domain.model.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

public class EmptyPayloadException extends IllegalArgumentException{

    private String msg = "Empty payload";

    @Override
    public String getMessage() {
        return msg;
    }
}
