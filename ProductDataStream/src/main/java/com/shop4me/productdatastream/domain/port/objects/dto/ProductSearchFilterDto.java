package com.shop4me.productdatastream.domain.port.objects.dto;

public interface ProductSearchFilterDto {

    String getProperty();

    String getOperator();

    String getValue();

    String where();
}
