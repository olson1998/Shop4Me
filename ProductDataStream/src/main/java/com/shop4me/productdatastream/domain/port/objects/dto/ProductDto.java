package com.shop4me.productdatastream.domain.port.objects.dto;

import com.shop4me.productdatastream.domain.port.objects.dao.ProductDao;

public interface ProductDto {

    Long getId();

    String getName();

    ProductDao toDao();

    void setTenantId(int tenantId);
}
