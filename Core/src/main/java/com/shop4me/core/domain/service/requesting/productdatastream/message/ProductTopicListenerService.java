package com.shop4me.core.domain.service.requesting.productdatastream.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.model.request.utils.ProductSearchFilter;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.productdatastream.utils.ProductSearchFilterDto;
import com.shop4me.core.domain.port.web.messaging.ProductTopicListenerRepository;
import com.shop4me.core.domain.port.web.messaging.MessageProducerRepository;
import com.shop4me.core.domain.service.processing.utils.PayloadWriter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.UUID;

import static com.shop4me.core.domain.model.request.keys.Operation.*;

@RequiredArgsConstructor
public class ProductTopicListenerService implements ProductTopicListenerRepository {

    private final String productOutboundTopic;

    private final ObjectMapper objectMapper;

    private final MessageProducerRepository messageProducerRepository;

    @Override
    public String requestSavingProduct(int tenantId, Map<String, ProductDto> productSavingMap) {
        var correlationId= UUID.randomUUID().toString();
        var payload = createPayload(productSavingMap);
        messageProducerRepository.send(correlationId, productOutboundTopic, tenantId, SAVE.name() ,payload);
        return correlationId;
    }

    @Override
    public String requestObtainingProducts(int tenantId, long[] productIds) {
        var correlationId= UUID.randomUUID().toString();
        var payload = createPayload(productIds);
        messageProducerRepository.send(correlationId, productOutboundTopic, tenantId, OBTAIN.name() ,payload);
        return correlationId;
    }

    @Override
    public String requestSearchingProduct(int tenantId, ProductSearchFilterDto[] productSearchFilters) {
        var correlationId= UUID.randomUUID().toString();
        var payload = createPayload(productSearchFilters);
        messageProducerRepository.send(correlationId, productOutboundTopic, tenantId, SEARCH.name() ,payload);
        return correlationId;
    }

    @Override
    public String requestMultiSearchProduct(int tenantId, MultiValueMap<String, ProductSearchFilter> multiSearchFilters) {
        var correlationId= UUID.randomUUID().toString();
        var payload = createPayload(multiSearchFilters);
        messageProducerRepository.send(correlationId, productOutboundTopic, tenantId, "SEARCH_MANY" ,payload);
        return correlationId;
    }

    @Override
    public String requestEditingProduct(int tenantId, Map<String, String> productEditMap) {
        var correlationId= UUID.randomUUID().toString();
        var payload = createPayload(productEditMap);
        messageProducerRepository.send(correlationId, productOutboundTopic, tenantId, EDIT.name(), payload);
        return correlationId;
    }

    @SneakyThrows
    private String createPayload(@NonNull Object payload){
        var json = objectMapper.writeValueAsString(payload);
        return PayloadWriter.write(json);
    }
}
