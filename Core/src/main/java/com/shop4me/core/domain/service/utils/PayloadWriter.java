package com.shop4me.core.domain.service.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PayloadWriter {

    public static String write(String payload){
        var base64Bytes = Base64.getEncoder().encode(payload.getBytes(StandardCharsets.UTF_8));
        return new String(base64Bytes);
    }
}
