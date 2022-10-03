package com.shop4me.productdatastream.domain.port.requesting.handler;

import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;

import java.util.concurrent.CompletableFuture;

public interface CategoryRequestHandler {

    CompletableFuture<Object> handle(CoreRequest request);
}
