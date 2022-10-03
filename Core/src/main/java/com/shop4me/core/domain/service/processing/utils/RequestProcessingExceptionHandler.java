package com.shop4me.core.domain.service.processing.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static java.util.Map.entry;

@Slf4j

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestProcessingExceptionHandler {

    public static Map<String, Integer> returnEmptyMapOfAffectedRows(Throwable e){
        log.error(e.toString());
        return Map.ofEntries(
                entry("affected_rows", 0)
        );
    }

    public static Map<String, String> returnEmptyMap(Throwable e){
        log.error(e.toString());
        return Map.ofEntries();
    }
}
