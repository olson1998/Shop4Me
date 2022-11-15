package com.shop4me.core.domain.service.requesting.productdatastream.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.web.messaging.CategoryTopicListenerRepository;
import com.shop4me.core.domain.port.web.messaging.MessageProducerRepository;
import com.shop4me.core.domain.service.processing.utils.PayloadWriter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.UUID;

import static com.shop4me.core.domain.model.request.keys.Operation.OBTAIN;
import static com.shop4me.core.domain.model.request.keys.Operation.SAVE;

@RequiredArgsConstructor
public class CategoryTopicListenerService implements CategoryTopicListenerRepository {

    private final String categoryOutboundTopic;

    private final ObjectMapper objectMapper;

    private final MessageProducerRepository messageProducerRepository;

    @Override
    public String requestSavingCategories(int tenantId, Map<String, CategoryDto> categorySaveMap) {
        var id = UUID.randomUUID().toString();
        var payload = createPayload(categorySaveMap);
        messageProducerRepository.send(id, categoryOutboundTopic, tenantId, SAVE.name() ,payload);
        return id;
    }

    @Override
    public String requestObtainingCategories(int tenantId) {
        var id = UUID.randomUUID().toString();
        messageProducerRepository.send(id, categoryOutboundTopic, tenantId, OBTAIN.name(), null);
        return id;
    }

    @SneakyThrows
    private String createPayload(@NonNull Object payload){
        var json = objectMapper.writeValueAsString(payload);
        return PayloadWriter.write(json);
    }
}
