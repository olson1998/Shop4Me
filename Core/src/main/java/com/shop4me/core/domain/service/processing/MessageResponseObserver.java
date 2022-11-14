package com.shop4me.core.domain.service.processing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class MessageResponseObserver {

    private final String messageId;

    private boolean responseReceived = false;

    public void waitUntilResponse() {
        log.trace("OBSERVATION FOR MESSAGE: '{}' STARTED AT: {}", messageId, LocalDateTime.now());
        synchronized (this){
            while (!responseReceived){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        log.trace("RESPONSE FOR MESSAGE: '{}' RECEIVED AT {}", messageId, LocalDateTime.now());
    }

    public synchronized void notifyReceiving(){
        this.notify();
        this.responseReceived = true;
    }
}
