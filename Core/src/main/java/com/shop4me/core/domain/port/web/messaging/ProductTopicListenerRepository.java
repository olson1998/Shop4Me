package com.shop4me.core.domain.port.web.messaging;

import com.shop4me.core.domain.model.request.utils.ProductSearchFilter;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.productdatastream.utils.ProductSearchFilterDto;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface ProductTopicListenerRepository {

    String requestSavingProduct(int tenantId, Map<String, ProductDto> productSavingMap);

    String requestObtainingProducts(int tenantId, long[]productIds);

    String requestSearchingProduct(int tenantId, ProductSearchFilterDto[] productSearchFilters);

    String requestMultiSearchProduct(int tenantId, MultiValueMap<String, ProductSearchFilter> multiSearchFilters);

    String requestEditingProduct(int tenantId, Map<String, String> productEditMap);
}
