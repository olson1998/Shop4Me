package com.shop4me.productdatastream.domain.service.requesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.port.requesting.handler.InboundMessageProcessor;
import com.shop4me.productdatastream.domain.port.requesting.handler.RequestHandler;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.messaging.OutboundMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class InboundMessageProcessingService implements InboundMessageProcessor {

    private final ObjectMapper objectMapper;
    private final RequestHandler requestHandler;

    private final OutboundMessageProducer outboundMessageProducer;

    @Override
    public void process(InboundMsg inboundMsg) {
        CompletableFuture.supplyAsync(()-> requestHandler.handle(inboundMsg))
                .thenAccept(payload-> produceOutboundOkMessage(inboundMsg, payload))
                .exceptionally(error-> produceOutboundErrorMessage(inboundMsg, error));
    }

    private void produceOutboundOkMessage(InboundMsg inboundMsg, Object object){
        var payload = writePayload(object);
        outboundMessageProducer.produce(inboundMsg, "OK", payload);
    }

    private Void produceOutboundErrorMessage(InboundMsg inboundMsg, Throwable error){
        var header = error.getCause().getClass().getName();
        var payload = writePayload(error.getMessage());
        outboundMessageProducer.produce(inboundMsg, "ERROR", header, payload);
        return null;
    }

    @SneakyThrows
    private String writePayload(Object payload){
        if(payload != null){
            var payloadJson = objectMapper.writeValueAsString(payload);
            var payloadBase64Bytes = Base64.getEncoder().encode(payloadJson.getBytes(StandardCharsets.UTF_8));
            return new String(payloadBase64Bytes);
        }else {
            return null;
        }
    }
}
