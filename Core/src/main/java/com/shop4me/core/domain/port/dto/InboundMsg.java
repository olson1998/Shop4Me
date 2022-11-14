package com.shop4me.core.domain.port.dto;

public interface InboundMsg {

    String getMessageId();

    String getProcessingStatus();

    String getHeader();

    String getPayload();

}
