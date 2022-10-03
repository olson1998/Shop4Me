package com.shop4me.core.domain.port.web.datastream.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ProductRepository {

    Mono<Map<String, String>> requestSavingProducts(ProductDto[] products);
}
