package com.shop4me.productdatastream.domain.port.persisting.dao;

import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductDto;

public interface ProductDao {

    Long getId();

    String getName();

    ProductDto toDto();

}
