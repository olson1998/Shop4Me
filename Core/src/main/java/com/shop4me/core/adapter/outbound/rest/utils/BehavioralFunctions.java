package com.shop4me.core.adapter.outbound.rest.utils;

import com.shop4me.core.adapter.outbound.rest.exception.Shop4MeCoreRequestFailed;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.shop4me.core.adapter.outbound.rest.utils.TypeReference.MAP_STR_STR_REF;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BehavioralFunctions {

    public static final Function<ClientResponse, Mono<? extends Throwable>> ON_BAD_REQUEST = response ->
            response.bodyToMono(MAP_STR_STR_REF)
            .map(exceptionMap -> exceptionMap.get("exception"))
            .map(Shop4MeCoreRequestFailed::new);
}
