package com.shop4me.core.model;

import java.util.Map;
import java.util.UUID;

import static java.util.Map.entry;

public class ConstantTestResponses {

    public static final Map<String, Integer> AFFECTED_ROWS_ONE = Map.ofEntries(
            entry("affected_rows", 1)
    );

    public static final Map<String, Integer> AFFECTED_ROWS_ZERO= Map.ofEntries(
            entry("affected_rows", 0)
    );

    public static final Map<String, String> CATEGORIES_SAVE_SUCCESS_RESPONSE = Map.ofEntries(
            entry("\"all\".\"test-1\"", "SUCCESS"),
            entry("\"all\".\"test-2\"", "SUCCESS")
    );

    public static final Map<String, String> CATEGORIES_SAVE_FAILED_RESPONSE = Map.ofEntries(
            entry("\"all\".\"test-1\"", "FAILURE"),
            entry("\"all\".\"test-2\"", "FAILURE")
    );

    public static final Map<String, String> CATEGORIES_SAVE_PARTLY_SUCCESS_RESPONSE = Map.ofEntries(
            entry("\"all\".\"test-1\"", "SUCCESS"),
            entry("\"all\".\"test-2\"", "FAILURE")
    );
}
