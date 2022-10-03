package com.shop4me.productdatastream.domain.model.request;

import com.shop4me.productdatastream.domain.model.exception.EmptyPayloadException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmptyPayloadCheck {

    public static void scan(String payload, String emptyJsonObjPattern){
        if(payload.equals(emptyJsonObjPattern)){
            throw new EmptyPayloadException();
        }
    }
}
