package com.shop4me.core.adapter.outbound.messaging;

import com.shop4me.core.application.dto.OutboundMessage;
import com.shop4me.core.domain.port.web.messaging.MessageProducerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

@Slf4j

@Service
@RequiredArgsConstructor
public class MessageProducerServiceRepository implements MessageProducerRepository {

    private final ProducerFactory<String, OutboundMessage> producerFactory;

    @Override
    public void send(String messageId, String topic, int tenantId, String operation, String payload) {
        var outboundMsg = new OutboundMessage(messageId, tenantId, operation, payload);
        log.trace("SENDING TO: '{}' REQUEST: {}", topic, outboundMsg);
        try(var producer = producerFactory.createProducer()){
            var record = new ProducerRecord<String, OutboundMessage>(topic, outboundMsg);
            producer.send(record);
        }
    }
}
