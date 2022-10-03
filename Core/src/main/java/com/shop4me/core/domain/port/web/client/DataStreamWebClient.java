package com.shop4me.core.domain.port.web.client;

import org.springframework.web.reactive.function.client.WebClient;

public interface DataStreamWebClient {

    WebClient get();

    void setUp();
}
