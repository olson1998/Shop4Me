package com.shop4me.productdatastream.domain.port.objects.dao;

import com.shop4me.productdatastream.domain.port.objects.dto.ProductDto;

public interface ProductDao {

    Long getId();

    String getName();

    ProductDto toDto();

    void setTenantId(int tenantId);
}
