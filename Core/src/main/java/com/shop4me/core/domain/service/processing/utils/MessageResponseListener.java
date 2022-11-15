package com.shop4me.core.domain.service.processing.utils;

import com.shop4me.core.domain.port.processing.ResponseListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class MessageResponseListener implements ResponseListener {

    private final String messageId;

    private boolean responseReceived = false;

    @Override
    public void waitUntilResponse() {
        log.trace("LISTENING FOR RESPONSE OF MESSAGE: '{}' STARTED AT: {}", messageId, LocalDateTime.now());
        synchronized (this){
            while (!responseReceived){
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        log.trace("LISTENING FOR RESPONSE OF MESSAGE: '{}' RECEIVED AT {}", messageId, LocalDateTime.now());
    }

    @Override
    public synchronized void notifyReceiving(){
        this.notifyAll();
        this.responseReceived = true;
    }
}
