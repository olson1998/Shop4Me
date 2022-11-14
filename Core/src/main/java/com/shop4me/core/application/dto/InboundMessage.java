package com.shop4me.core.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.InboundMsg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor
@AllArgsConstructor
public class InboundMessage implements InboundMsg {

    @JsonProperty(value = "id", required = true)
    private String messageId;

    @JsonProperty(value = "status", required = true)
    private String processingStatus;

    @JsonProperty(value = "header")
    private String header;

    @JsonProperty(value = "payload")
    private String payload;
}
