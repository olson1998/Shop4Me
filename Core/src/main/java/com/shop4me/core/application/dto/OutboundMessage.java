package com.shop4me.core.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OutboundMessage {

    @JsonProperty(value = "id")
    private final String messageId;

    @JsonProperty(value = "tenant", required = true)
    private final int tenantId;

    @JsonProperty(value = "operation", required = true)
    private final String operation;

    @JsonProperty(value = "payload", required = true)
    private final String payload;

    @Override
    public String toString() {
        return "OutboundMessage{" +
                "messageId='" + messageId + '\'' +
                ", tenantId=" + tenantId +
                ", operation='" + operation + '\'' +
                ", payload=" + payload.length() + " chars" +
                '}';
    }
}
