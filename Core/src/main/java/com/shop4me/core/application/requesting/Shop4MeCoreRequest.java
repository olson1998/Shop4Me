package com.shop4me.core.application.requesting;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class Shop4MeCoreRequest {

    @JsonProperty(value = "id", required = true)
    private final String requestId = UUID.randomUUID().toString();
    @JsonProperty(value = "entity", required = true)
    private final String entity;

    @JsonProperty(namespace = "operation", required = true)
    private final String operation;

    @JsonProperty(namespace = "payload", required = true)
    private final String payload;

    @Override
    public String toString() {
        return "Shop4MeCoreRequest{" +
                "entity=" + entity +
                ", operation=" + operation +
                ", payload=" + payload.length() +
                " chars}";
    }

}
