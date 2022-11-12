package com.shop4me.productdatastream.domain.model.request.category;

import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.requesting.CategoryObtainRequest;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)

public class CategoryObtainRequestImpl implements CategoryObtainRequest {

    private final int tenantId;
    @Override
    public String toString() {
        return "OBTAIN ALL CATEGORIES";
    }

    @Override
    public String writeJpql() {
        return "select c from CategoryEntity c where c.tenantId="+tenantId;
    }

    public static CategoryObtainRequestImpl fromInboundMessage(@NonNull InboundMsg inboundMsg){
        return new CategoryObtainRequestImpl(inboundMsg.getTenantId());
    }
}
