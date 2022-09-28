package com.shop4me.productdatastream.domain.port.requesting;

import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductDto;

public interface ProductDeleteRequest {

    ProductDto getProduct();

    String writeJpqlQuery();
}
