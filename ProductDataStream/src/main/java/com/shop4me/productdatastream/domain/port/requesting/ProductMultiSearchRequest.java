package com.shop4me.productdatastream.domain.port.requesting;

import java.util.Map;

public interface ProductMultiSearchRequest {

    int getTenantId();

    Map<String, ProductSearchRequest> getMultiSearchMap();
}
