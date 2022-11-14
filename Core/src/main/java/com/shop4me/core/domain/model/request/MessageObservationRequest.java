package com.shop4me.core.domain.model.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MessageObservationRequest implements Runnable {

    private final String id;

    private boolean messageReceived = false;

    @Override
    public void run() {
        log.info("Received new observation request: {}", id);
        while (!messageReceived){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void notifyInboundMessageReceived(){
        log.info("Received response to: {}", id);
        this.messageReceived = true;
    }
}
