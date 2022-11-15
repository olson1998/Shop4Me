package com.shop4me.core.adapter.outbound.rest;

import com.shop4me.core.adapter.outbound.rest.exception.PingFalseResponse;
import com.shop4me.core.adapter.outbound.rest.exception.PingNotResponding;
import com.shop4me.core.domain.port.web.client.DataStreamWebClientFactory;
import com.shop4me.core.domain.port.web.client.WebClientFabricationPlan;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.shop4me.core.adapter.outbound.rest.utils.PredicateHttpResponseCode.NOT_FOUND;
import static com.shop4me.core.adapter.outbound.rest.utils.TypeReference.MAP_STR_STR_REF;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j

@Service
public class Shop4MeCoreWebClientFactory implements DataStreamWebClientFactory {

    @Override
    public CompletableFuture<WebClient> fabricate(WebClientFabricationPlan plan){
        return livenessCheckProbe(plan)
                .then(authenticateCore(plan))
                .mapNotNull(tokens -> {
                    if(tokens.get("access_token") != null){
                        return buildWebClient(plan, tokens);
                    }else {
                        log.warn("Web client for component: {} has not been authenticated...", plan.getComponent());
                        log.warn("Creating default unauthenticated client for component: {}", plan.getComponent());
                        return plan.getBaseProduct();
                    }
                }).toFuture();
    }

    private Mono<Boolean> livenessCheckProbe(WebClientFabricationPlan plan){
        var call = new AtomicInteger(0);
        var max = new AtomicInteger(5);
        return plan.getBaseProduct()
                .get()
                .uri("/")
                .retrieve()
                .onStatus(NOT_FOUND, r->
                        Mono.error(new PingNotResponding(plan.getUrl(), plan.getComponent()))
                )
                .bodyToMono(String.class)
                .doOnError(PingNotResponding.class, logPingNotResponding(plan, call, max))
                .doOnError(WebClientRequestException.class, logPingNotResponding(plan, call, max))
                .doOnSuccess(logPingResponding(plan, call, max))
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(5))
                        .filter(PingNotResponding.class::isInstance)
                        .filter(WebClientRequestException.class::isInstance))
                .mapNotNull(response -> checkLivenessProbeResult(plan, response, call, max));
    }

    @SneakyThrows
    private Mono<Map<String, String>> authenticateCore(WebClientFabricationPlan plan){
        var loginForm = new LinkedMultiValueMap<String, String>();
        loginForm.put("username", List.of(plan.getUsername()));
        loginForm.put("password", List.of(plan.getPassword()));

        return plan.getBaseProduct().post()
                .uri("/lg")
                .header(AUTHORIZATION)
                .contentType(APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(loginForm))
                .attribute("username", plan.getUsername())
                .attribute("password", plan.getPassword())
                .retrieve()
                .bodyToMono(MAP_STR_STR_REF)
                .doOnRequest(e-> log.info("Attempted authentication of Shop4Me.Core in Component: {}, url: {}",
                        plan.getComponent(),
                        plan.getUrl()
                ))
                .doOnSuccess(tokens -> log.trace("Authentication finished"));
    }

    @SneakyThrows
    private WebClient buildWebClient(WebClientFabricationPlan plan, Map<String, String> tokenMap){
        var client = new AtomicReference<>(plan.getBaseProduct());
        var token = tokenMap.get("access_token");
        if(token == null){
            log.warn("Created unauthenticated REST API service for component: {}, url: {}",
                    plan.getComponent(),
                    plan.getUrl()
            );
        }
        else{
            log.info("Creating authenticated web client service for component: {}, url: {}",
                    plan.getComponent(),
                    plan.getUrl()
            );
            var authVal = "Barrier " + token;

            var authClient = WebClient.builder()
                    .baseUrl(plan.getUrl())
                    .defaultHeader(CONTENT_TYPE, String.valueOf(APPLICATION_JSON))
                    .defaultHeader(AUTHORIZATION, authVal)
                    .build();
            client.set(authClient);
        }
        return client.get();
    }

    private boolean checkLivenessProbeResult(WebClientFabricationPlan plan, String response, AtomicInteger call, AtomicInteger max){
        if(response.equals("OK")){
            log.info("component: {}, call [{}/{}], status: Connection has been established",
                    plan.getComponent(),
                    call,
                    max
            );
            return true;
        }else {
            log.error("component: {}, call [{}/{}], status: Connection has not been established",
                    plan.getComponent(),
                    call,
                    max
            );
            throw new PingFalseResponse(plan.getComponent(), response);
        }
    }

    private Consumer<Throwable> logPingNotResponding(WebClientFabricationPlan plan, AtomicInteger call, AtomicInteger max){
        return e-> {
            var c =call.getAndIncrement();
            if(c == 5){ call.set(0);}
            log.error("component: {}, call [{}/{}], status: {}",
                    plan.getComponent(), c,
                    max,
                    e.getMessage()
            );
        };
    }

    private Consumer<String> logPingResponding(WebClientFabricationPlan plan, AtomicInteger call, AtomicInteger max){
        return e-> {
            var c =call.getAndIncrement();
            if(c == 5){ call.set(0);}
            log.info("component: {}, call [{}/{}], status: Service Found", plan.getComponent(), c, max);
        };
    }

}
