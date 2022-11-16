package com.shop4me.productdatastream.domain.model.request.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.model.request.product.utils.ProductSearchFilter;
import com.shop4me.productdatastream.domain.model.request.utils.RequestPayloadReader;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.requesting.ProductMultiSearchRequest;
import com.shop4me.productdatastream.domain.port.requesting.ProductSearchRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.LinkedMultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMultiSearchRequestImpl implements ProductMultiSearchRequest {

    private final int tenantId;

    private final Map<String, ProductSearchRequest> multiSearchMap;

    @SneakyThrows
    public static ProductMultiSearchRequest fromInboundMessage(InboundMsg inboundMsg){
        var json = inboundMsg.getDecodedPayload();

        EmptyPayloadCheck.scan(json, "{}");

        var multiSearchFiltersMap = new HashMap<String, ProductSearchRequest>();

        var multiSearchProduct = RequestPayloadReader.OBJECT_MAPPER
                .readValue(json, new TypeReference<LinkedMultiValueMap<String, ProductSearchFilter>>() {
                });
        multiSearchProduct.forEach((correlationId, filters)->{
            var request = new ProductSearchRequestImpl(filters, inboundMsg.getTenantId());
            multiSearchFiltersMap.put(correlationId, request);
        });
        return new ProductMultiSearchRequestImpl(inboundMsg.getTenantId(), multiSearchFiltersMap);
    }

}
