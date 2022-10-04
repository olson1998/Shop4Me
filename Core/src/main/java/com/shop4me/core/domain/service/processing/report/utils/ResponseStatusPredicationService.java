package com.shop4me.core.domain.service.processing.report.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseStatusPredicationService {

    public static Predicate<Map<String, String>> ONLY_SUCCESS_RESPONSES = responseMap ->
            responseMap.values().stream()
                    .allMatch(status -> status.equals("SUCCESS"));

    public static Predicate<Map<String, String>> ONLY_FAILURE_RESPONSES = responseMap ->
            responseMap.values().stream()
                    .allMatch(status -> status.equals("FAILURE"));
}
