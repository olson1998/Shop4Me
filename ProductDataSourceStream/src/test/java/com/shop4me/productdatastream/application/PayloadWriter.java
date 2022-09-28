package com.shop4me.productdatastream.application;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.shop4me.productdatastream.domain.model.request.toolset.RequestPayloadReader.OBJECT_MAPPER;

public class PayloadWriter {

    public static String write(Object payload){
        try{
            var json = OBJECT_MAPPER.writeValueAsString(payload);
            var payloadBytes = Base64.getEncoder().encode(json.getBytes(StandardCharsets.UTF_8));
            return new String(payloadBytes);
        }catch (JsonProcessingException e){
            throw new IllegalArgumentException();
        }
    }
}
