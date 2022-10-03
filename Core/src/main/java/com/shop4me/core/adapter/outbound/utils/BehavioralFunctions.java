package com.shop4me.core.adapter.outbound.utils;

import com.shop4me.core.adapter.outbound.exception.Shop4MeCoreRequestFailed;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.shop4me.core.adapter.outbound.utils.TypeReference.MAP_STR_OBJ_REF;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BehavioralFunctions {

    public static final Function<ClientResponse, Mono<? extends Throwable>> ON_BAD_REQUEST = response ->
            response.bodyToMono(MAP_STR_OBJ_REF)
            .map(exceptionMap -> (String) exceptionMap.get("exception"))
            .map(Shop4MeCoreRequestFailed::new);
}
