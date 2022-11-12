package com.shop4me.productdatastream.domain.model.request.product;

import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.model.request.utils.RequestPayloadReader;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.requesting.ProductDeleteRequest;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)

public class ProductDeleteRequestImpl implements ProductDeleteRequest {

    private final long[] productsIds;

    private final int tenantId;

    @Override
    public String writeJpqlQuery(){
        var query = new StringBuilder("delete from ProductEntity p where p.tenantId=")
                .append(tenantId)
                .append(" and (");
        for(int i =0; i < productsIds.length; i++){
            query.append("p.id=")
                    .append(productsIds[i])
                    .append(' ');
            if(i < productsIds.length -1){
                query.append("or ");
            }
        }
        query.append(")");
        return query.toString();
    }

    @Override
    public String toString() {
        return "DELETE PRODUCT ID: " + Arrays.toString(productsIds);
    }

    @SneakyThrows
    public static ProductDeleteRequestImpl fromInboundMessage(@NonNull InboundMsg inboundMsg){
        var payload = inboundMsg.getDecodedPayload();

        EmptyPayloadCheck.scan(payload, "[]");

        var productsIdsToDelete = RequestPayloadReader.OBJECT_MAPPER
                .readValue(payload, long[].class);

        return new ProductDeleteRequestImpl(productsIdsToDelete, inboundMsg.getTenantId());
    }
}
