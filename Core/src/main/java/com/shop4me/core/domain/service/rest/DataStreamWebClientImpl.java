package com.shop4me.core.domain.service.rest;

import com.shop4me.core.domain.model.exception.WebClientNotSetUpException;
import com.shop4me.core.domain.model.rest.WebClientFabricationPlanImpl;
import com.shop4me.core.domain.port.web.client.DataStreamWebClient;
import com.shop4me.core.domain.port.web.client.DataStreamWebClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

import static com.shop4me.core.domain.model.component.Shop4MeComponent.PRODUCT_DATA_STREAM;

@Slf4j

public class DataStreamWebClientImpl implements DataStreamWebClient {

    private final WebClientFabricationPlanImpl webClientFabricationPlanImpl;

    private final DataStreamWebClientFactory dataStreamWebClientFactory;

    private WebClient productDataStreamWebClient;

    private boolean clientSettingUp = false;

    @Override
    public WebClient get() {
        var component = webClientFabricationPlanImpl.getComponent();
        if(!clientSettingUp && productDataStreamWebClient != null){
            return productDataStreamWebClient;
        } else if (!clientSettingUp && productDataStreamWebClient == null) {
            log.warn("Client for component {} has not been set up yet", PRODUCT_DATA_STREAM);
            this.setUp();
            throw new WebClientNotSetUpException(component);
        } else if (clientSettingUp && productDataStreamWebClient == null) {
            log.warn("Client for component {} has not been set up yet", PRODUCT_DATA_STREAM);
            throw new WebClientNotSetUpException(component);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void setUp() {
        this.clientSettingUp = true;

        log.info("Attempt to fabricate: {}",
                webClientFabricationPlanImpl
        );
        initFabrication()
                .fabricate(webClientFabricationPlanImpl)
                .thenAccept(this::finishFabrication)
                .exceptionally(e-> {
                    log.error(e.getMessage());
                    this.clientSettingUp = false;
                    return null;
                });
    }

    private DataStreamWebClientFactory initFabrication(){
        this.clientSettingUp = true;
        return this.dataStreamWebClientFactory;
    }

    private void finishFabrication(WebClient webClient){
        this.clientSettingUp = false;
        this.productDataStreamWebClient = webClient;
    }

    public DataStreamWebClientImpl(DataStreamWebClientFactory dataStreamWebClientFactory,
                                   String component,
                                   String url,
                                   String username,
                                   String password) {
        this.dataStreamWebClientFactory = dataStreamWebClientFactory;
        this.webClientFabricationPlanImpl = new WebClientFabricationPlanImpl(
                component,
                url,
                username,
                password
        );
    }

}
