package com.shop4me.productdatastream.application.requesting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.productdatastream.application.messaging.InboundMessage;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class Shop4MeCoreRequest implements CoreRequest {

    @JsonProperty("id")
    private String id;

    @Setter
    @JsonIgnore
    private int tenantId;

    @JsonProperty(value = "entity", required = true)
    private String entity;

    @JsonProperty(namespace = "operation", required = true)
    private String operation;

    @JsonProperty(namespace = "payload", required = true)
    private String payload;

    @Override
    public InboundMsg toInboundMessage() {
        return new InboundMessage(id, null, tenantId, operation, payload);
    }

    @Override
    public String toString() {
        return "Shop4MeCoreRequest{" +
                "entity=" + entity +
                ", operation=" + operation +
                ", payload=" + payload.length() +
                " chars}";
    }
}
