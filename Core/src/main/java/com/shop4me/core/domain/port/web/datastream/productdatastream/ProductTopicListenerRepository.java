package com.shop4me.core.domain.port.web.datastream.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;

import java.util.Map;

public interface ProductTopicListenerRepository {

    String requestSavingProduct(int tenantId, Map<String, ProductDto> productSavingMap);

    String requestEditingProduct(int tenantId, Map<String, String> productEditMap);

    String requestObtainingProduct(int tenantId, long[] productIds);
}
