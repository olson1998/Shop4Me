package com.shop4me.core.domain.service.requesting.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.web.datastream.ProductDataStream;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j

@AllArgsConstructor
public class ProductService implements ProductRepository {

    private final ProductDataStream productDataStream;

    @Override
    public Mono<Map<String, String>> requestSavingProducts(ProductDto[] products){
        var saveProductsMap = createSavingProductMap(products);
        return productDataStream.requestSavingProducts(saveProductsMap);
    }

    private Map<String, ProductDto> createSavingProductMap(@NonNull ProductDto[] products){
        var editMap = new HashMap<String, ProductDto>();
        Arrays.stream(products).forEach(product -> editMap.put(UUID.randomUUID().toString(), product));
        return editMap;
    }
}
