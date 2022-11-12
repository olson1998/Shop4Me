package com.shop4me.productdatastream.application.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Getter

@NoArgsConstructor
@AllArgsConstructor
public class InboundMessage implements InboundMsg {

    @JsonProperty(value = "id", required = true)
    private String messageId;

    @JsonIgnore
    private String topic;

    @JsonProperty(value = "tenant", required = true)
    private int tenantId;

    @JsonProperty(value = "operation", required = true)
    private String operation;

    @JsonProperty(value = "payload", required = true)
    private String payload;

    @Override
    public void setTopic(String topic) {
        this.topic=topic;
    }

    @Override
    @JsonIgnore
    public String getDecodedPayload() {
        var decodedPayloadBytes = Base64.getDecoder().decode(payload);
        return new String(decodedPayloadBytes);
    }

}
