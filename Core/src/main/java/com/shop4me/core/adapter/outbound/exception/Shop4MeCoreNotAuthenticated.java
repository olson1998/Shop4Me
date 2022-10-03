package com.shop4me.core.adapter.outbound.exception;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class Shop4MeCoreNotAuthenticated extends Throwable{

    private final String request;

    private final String component;

    @Override
    public String getMessage() {
        return request + "failed... Reason: Component: "+ component +
                " responded with Http Status: 403, because Shop4Me Core has not been successfully authenticated";
    }
}
