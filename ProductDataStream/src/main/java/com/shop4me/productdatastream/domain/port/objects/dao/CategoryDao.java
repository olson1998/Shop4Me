package com.shop4me.productdatastream.domain.port.objects.dao;

import com.shop4me.productdatastream.domain.port.objects.dto.CategoryDto;

public interface CategoryDao {

    CategoryDto toDto();

    Long getId();

    String getName();

    String getPath();
}
