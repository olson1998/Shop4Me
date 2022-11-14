package com.shop4me.core.domain.service.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PayloadReader {

    public static String read(String base64){
        var jsonBytes = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
        return new String(jsonBytes);
    }
}
