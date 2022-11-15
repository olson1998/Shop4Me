package com.shop4me.core.adapter.outbound.rest.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PredicateHttpResponseCode {
    
    public static final Predicate<HttpStatus> OK = httpStatus -> httpStatus.value() == 200;

    public static final Predicate<HttpStatus> NOT_FOUND = httpStatus -> httpStatus.value() == 404;

    public static final Predicate<HttpStatus> FORBIDDEN  = httpStatus -> httpStatus.value() ==403;

    public static final Predicate<HttpStatus> BAD_REQUEST  = httpStatus -> httpStatus.value() ==400;
}
