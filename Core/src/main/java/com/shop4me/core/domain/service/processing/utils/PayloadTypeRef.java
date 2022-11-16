package com.shop4me.core.domain.service.processing.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;

import java.lang.reflect.Type;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PayloadTypeRef {

    public static final TypeReference<Map<String, String>> MAP_STR_STR_REF = new TypeReference<>() {
    };

    public static final TypeReference<LinkedMultiValueMap<String, Long>> MULTI_MAP_STR_LONG = new TypeReference<>() {
    };
}
