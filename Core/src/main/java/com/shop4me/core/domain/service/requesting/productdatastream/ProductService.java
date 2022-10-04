package com.shop4me.core.domain.service.requesting.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.web.datastream.ProductDataStream;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j

@AllArgsConstructor
public class ProductService implements ProductRepository {

    private final ProductDataStream productDataStream;

    @Override
    public Mono<Map<String, String>> requestSavingProducts(Map<String, ProductDto> saveProductsMap){
        return productDataStream.requestSavingProducts(saveProductsMap);
    }

    @Override
    public Mono<Map<String, Integer>> requestDeletingProduct(ProductDto product) {
        return productDataStream.requestDeletingProduct(product);
    }

    @Override
    public Mono<Map<String, Integer>> requestEditingProduct(Map<String, String> productPropertyNewValueMap) {
        return productDataStream.requestEditingProducts(productPropertyNewValueMap);
    }
}
