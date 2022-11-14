package com.shop4me.productdatastream.adapter.inbound.rest;

import com.shop4me.productdatastream.adapter.inbound.rest.exception.UnreadableTenantIdentificationException;
import com.shop4me.productdatastream.application.requesting.Shop4MeCoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.handler.RequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Async
@RestController

@RequiredArgsConstructor
public class Shop4MeCoreRequestController {

    private final RequestHandler requestHandler;

    @PostMapping(path = "/rq")
    public CompletableFuture<Object> processRequest(@RequestHeader("tenant") String tenant,
                                                    @RequestBody Shop4MeCoreRequest coreRequest){
        var tenantId= readTenantIdFromHeader(tenant);
        coreRequest.setTenantId(tenantId);
        return requestHandler.handle(coreRequest);
    }

    private int readTenantIdFromHeader(String tenant){
        try{
            return Integer.parseInt(tenant);
        }catch (NumberFormatException | NullPointerException e){
            throw new UnreadableTenantIdentificationException();
        }
    }
}
