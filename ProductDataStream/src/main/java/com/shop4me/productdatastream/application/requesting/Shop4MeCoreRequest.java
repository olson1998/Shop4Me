package com.shop4me.productdatastream.application.requesting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Getter

@AllArgsConstructor
@NoArgsConstructor

public class Shop4MeCoreRequest implements CoreRequest {

    @JsonProperty("entity")
    private String entity;

    @JsonProperty("operation")
    private String operation;

    @JsonProperty("payload")
    private String payload;

    @Override
    public Shop4MeCoreRequest decodePayload(){
        var decodedBytes = Base64.getDecoder().decode(this.payload);
        this.payload = new String(decodedBytes);
        return this;
    }

    @Override
    public String toString() {
        return "Shop4MeCoreRequest{" +
                "entity=" + entity +
                ", operation=" + operation +
                ", payload=" + payload.length() + " chars" + '}';
    }
}
