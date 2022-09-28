package com.shop4me.productdatastream.domain.service.requesting;

import com.shop4me.productdatastream.domain.model.exception.Shop4MeCoreRequestExecutionException;
import com.shop4me.productdatastream.domain.model.request.enumset.Entity;
import com.shop4me.productdatastream.domain.port.requesting.handler.CategoryRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.CoreRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.ProductRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.ReviewRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j

@AllArgsConstructor

public class Shop4MeCoreRequestService implements CoreRequestHandler {

    private final ProductRequestHandler productRequestHandler;

    private final CategoryRequestHandler categoryRequestHandler;

    private final ReviewRequestHandler reviewRequestHandler;

    @Override
    public CompletableFuture<Object> handle(CoreRequest request){
        return delegateRequest(request)
                .exceptionally(e-> responseWithStatus400(request, e));
    }

    private CompletableFuture<Object> delegateRequest(CoreRequest request){
        switch (Entity.valueOf(request.getEntity())){
            case PRODUCT -> {
                return productRequestHandler.handle(request);
            }
            case REVIEW -> {
                return reviewRequestHandler.handle(request);
            }
            case CATEGORY -> {
                return categoryRequestHandler.handle(request);
            }
        }
        throw new UnknownError();
    }

    @SneakyThrows
    private Map<String, Object> responseWithStatus400(CoreRequest request, Throwable exception){
        log.error("Exception occurred during execution of core request: {}, reason : {}",
                request.toString(),
                exception.toString()
        );
        throw new Shop4MeCoreRequestExecutionException(
                request.getEntity(),
                request.getOperation(),
                exception
        );
    }

}
