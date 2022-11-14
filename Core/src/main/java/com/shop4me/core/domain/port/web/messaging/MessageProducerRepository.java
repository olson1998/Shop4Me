package com.shop4me.core.domain.port.web.messaging;

public interface MessageProducerRepository {

    void send(String messageId, String topic, int tenantId, String operation, String payload);
}
