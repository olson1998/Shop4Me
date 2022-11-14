package com.shop4me.core.domain.port.web.datastream.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ProductRepository {

    Mono<Map<String, String>> requestSavingProducts(int tenantId, Map<String, ProductDto> products);

    Mono<Map<String, Integer>> requestDeletingProduct(int tenantId, ProductDto product);

    Mono<Map<String, Integer>> requestEditingProduct(int tenantId, Map<String, String> productPropertyNewValueMap);
}
