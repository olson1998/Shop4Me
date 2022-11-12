package com.shop4me.productdatastream.adapter.outbound;

import com.shop4me.productdatastream.application.messaging.OutboundMessage;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.messaging.OutboundMessageProducer;
import com.shop4me.productdatastream.domain.port.requesting.utils.OutboundTopicResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageProducer implements OutboundMessageProducer {

    private final ProducerFactory<String, OutboundMessage> producerFactory;

    private final OutboundTopicResolver outboundTopicResolver;

    @Override
    public void produce(@NonNull InboundMsg inboundMsg, String status, String header, String payload) {
        var topic = outboundTopicResolver.resolve(inboundMsg.getTopic());
        var outboundMsg = new OutboundMessage(topic, inboundMsg.getMessageId(), status, header, payload);
        try(var producer = producerFactory.createProducer()){
            producer.send(new ProducerRecord<>(topic, outboundMsg));
        }
    }

    @Override
    public void produce(InboundMsg inboundMsg, String status, String payload) {
        produce(inboundMsg, status, null, payload);
    }

}
