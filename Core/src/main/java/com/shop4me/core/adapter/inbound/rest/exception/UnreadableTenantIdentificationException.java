package com.shop4me.core.adapter.inbound.rest.exception;

public class UnreadableTenantIdentificationException extends NumberFormatException{

    @Override
    public String getMessage() {
        return "unreadable tenant identification";
    }
}
