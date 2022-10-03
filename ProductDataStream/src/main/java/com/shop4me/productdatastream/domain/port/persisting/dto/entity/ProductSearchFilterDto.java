package com.shop4me.productdatastream.domain.port.persisting.dto.entity;

public interface ProductSearchFilterDto {

    String getProperty();

    String getOperator();

    String getValue();

    String where();
}
