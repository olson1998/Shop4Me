package com.shop4me.core.domain.port.processing;

public interface ResponseListener {

    void waitUntilResponse();

    void notifyReceiving();
}
