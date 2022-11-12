package com.shop4me.productdatastream.domain.port.requesting;

import com.shop4me.productdatastream.domain.port.objects.dto.ProductDto;

import java.util.Map;

public interface ProductSaveRequest {

    Map<String, ProductDto> getProductsSaveMap();

}
