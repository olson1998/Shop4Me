package com.shop4me.core.adapter.outbound.rest.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeReference {

    public static final ParameterizedTypeReference<Map<String, String>> MAP_STR_STR_REF = new ParameterizedTypeReference<>(){};

    public static final ParameterizedTypeReference<Map<String, Integer>> MAP_STR_INT_REF = new ParameterizedTypeReference<>(){};
}
