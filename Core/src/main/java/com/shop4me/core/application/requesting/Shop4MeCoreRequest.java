package com.shop4me.core.application.requesting;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor

@EqualsAndHashCode
public class Shop4MeCoreRequest {

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
