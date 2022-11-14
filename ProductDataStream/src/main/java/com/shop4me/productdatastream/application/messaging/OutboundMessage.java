package com.shop4me.productdatastream.application.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.productdatastream.domain.port.messaging.OutboundMsg;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutboundMessage implements OutboundMsg {

    @JsonIgnore
    private final String topic;

    @JsonProperty(value = "id", required = true)
    private final String messageId;

    @JsonProperty(value = "status", required = true)
    private final String processingStatus;

    @JsonProperty(value = "header")
    private final String header;

    @JsonProperty(value = "payload", required = true)
    private final String payload;

    @Override
    public String getTopic() {
        return topic;
    }
}
