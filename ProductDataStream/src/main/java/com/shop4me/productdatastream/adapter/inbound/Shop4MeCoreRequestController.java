package com.shop4me.productdatastream.adapter.inbound;

import com.shop4me.productdatastream.application.requesting.Shop4MeCoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.handler.CoreRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Async
@RestController

@RequiredArgsConstructor

@RequestMapping(path = "/rq")
public class Shop4MeCoreRequestController {

    private final CoreRequestHandler shop4MeCoreRequestHandler;

    @PostMapping
    public CompletableFuture<Object> handleRequest(@RequestBody Shop4MeCoreRequest request){
        return shop4MeCoreRequestHandler.handle(request);
    }
}
