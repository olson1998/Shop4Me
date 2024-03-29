package com.shop4me.productdatastream.domain.service.requesting;

import com.shop4me.productdatastream.domain.model.request.enumset.Entity;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.handler.CategoryRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.ProductRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.RequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.ReviewRequestHandler;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.requesting.utils.EntityResolver;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j

@AllArgsConstructor

public class Shop4MeRequestService implements RequestHandler {

    private final ProductRequestHandler productRequestHandler;

    private final CategoryRequestHandler categoryRequestHandler;

    private final ReviewRequestHandler reviewRequestHandler;

    private final EntityResolver entityResolver;

    @Override
    public Object handle(@NonNull InboundMsg inboundMsg){
        return delegateRequest(inboundMsg);
    }

    @Override
    public CompletableFuture<Object> handle(CoreRequest request) {
        return CompletableFuture.supplyAsync(request::getEntity)
                .thenApply(entity-> executeRequest(entity, request.toInboundMessage()));
    }

    private Object delegateRequest(InboundMsg inboundMsg){
        var entity = entityResolver.resolve(inboundMsg.getTopic());
        return executeRequest(entity, inboundMsg);
    }
    private Object executeRequest(String entity, InboundMsg inboundMsg){
        switch (Entity.valueOf(entity)){
            case PRODUCT -> {
                return productRequestHandler.handle(inboundMsg);
            }
            case REVIEW -> {
                return reviewRequestHandler.handle(inboundMsg);
            }
            case CATEGORY -> {
                return categoryRequestHandler.handle(inboundMsg);
            }
        }
        throw new UnknownError();
    }

}
