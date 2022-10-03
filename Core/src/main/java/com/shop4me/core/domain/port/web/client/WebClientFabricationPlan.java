package com.shop4me.core.domain.port.web.client;

import org.springframework.web.reactive.function.client.WebClient;

public interface WebClientFabricationPlan {

    String getComponent();

    String getUrl();

    String getUsername();

    String getPassword();

    WebClient getBaseProduct();
}
