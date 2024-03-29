package com.shop4me.productdatastream.domain.model.request.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shop4me.productdatastream.domain.model.request.utils.RequestPayloadReader;
import com.shop4me.productdatastream.domain.model.dto.Product;
import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.objects.dto.ProductDto;
import com.shop4me.productdatastream.domain.port.requesting.ProductSaveRequest;
import lombok.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSaveRequestImpl implements ProductSaveRequest {

    @Getter
    private final Map<String, ProductDto> productsSaveMap;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("SAVE PRODUCT: (");
        List<String> productsCorrelationIdList = new java.util.ArrayList<>(List.copyOf(productsSaveMap.keySet()));
        productsCorrelationIdList.sort(Comparator.naturalOrder());
        int len = productsCorrelationIdList.size();
        int last = len - 1;
        for (int i = 0; i < len; i++) {
            String correlationId = productsCorrelationIdList.get(i);
            var productDto = productsSaveMap.get(correlationId);
            str.append("'").append(productDto.getName()).append("'");
            if (i < last) {
                str.append(", ");
            }
        }
        str.append(")");
        return str.toString();
    }

    @SneakyThrows
    public static ProductSaveRequestImpl fromInboundMessage(@NonNull InboundMsg inboundMsg) {
        var json = inboundMsg.getDecodedPayload();

        EmptyPayloadCheck.scan(json, "{}");

        var products = RequestPayloadReader.OBJECT_MAPPER
                .readValue(json, new TypeReference<Map<String, Product>>() {
                });

        var productsDtoMap = writeProductDtoMap(products, inboundMsg.getTenantId());

        return new ProductSaveRequestImpl(productsDtoMap);
    }

    private static Map<String, ProductDto> writeProductDtoMap(Map<String, Product> products, int tenantId) {
        var productsDtoMap = new HashMap<String, ProductDto>();

        products.keySet().forEach(correlationId -> {
            var product = products.get(correlationId);
            product.setCorrelationId(correlationId);
            product.setTenantId(tenantId);
            productsDtoMap.put(correlationId, product);
        });
        return productsDtoMap;
    }

}
