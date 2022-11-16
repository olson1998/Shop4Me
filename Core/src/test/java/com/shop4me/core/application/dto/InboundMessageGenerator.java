package com.shop4me.core.application.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.model.request.keys.RequestProcessingStatus;
import com.shop4me.core.domain.port.dto.InboundMsg;
import com.shop4me.core.domain.service.processing.utils.PayloadWriter;

import java.util.UUID;

public class InboundMessageGenerator {

    private static final ObjectMapper TEST_OBJECT_MAPPER= new ObjectMapper();

    public static InboundMsg generate(String messageId, RequestProcessingStatus status, String header, String payload){
        return new InboundMessage(
                messageId,
                status.name(),
                header,
                payload
        );
    }

    public static InboundMsg generate(RequestProcessingStatus status, String payload){
        return new InboundMessage(
                UUID.randomUUID().toString(),
                status.name(),
                null,
                payload
        );
    }

    public static InboundMsg generate(RequestProcessingStatus status, Object payloadObj){
        try{
            var json = TEST_OBJECT_MAPPER.writeValueAsString(payloadObj);
            var payload = PayloadWriter.write(json);
            return generate(status, payload);
        }catch (JsonProcessingException e){
            throw new IllegalArgumentException();
        }
    }

    public static InboundMsg generate(String correlationId, RequestProcessingStatus status, Object payloadObj){
        try{
            var json = TEST_OBJECT_MAPPER.writeValueAsString(payloadObj);
            var payload = PayloadWriter.write(json);
            return generate(correlationId, status, null, payload);
        }catch (JsonProcessingException e){
            throw new IllegalArgumentException();
        }
    }
}
