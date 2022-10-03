package com.shop4me.core.domain.port.web.client;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

public interface DataStreamWebClientFactory {

    CompletableFuture<WebClient> fabricate(WebClientFabricationPlan plan);
}
