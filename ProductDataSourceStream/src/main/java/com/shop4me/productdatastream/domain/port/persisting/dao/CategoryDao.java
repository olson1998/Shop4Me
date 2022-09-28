package com.shop4me.productdatastream.domain.port.persisting.dao;

import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;

public interface CategoryDao {

    CategoryDto toDto();

    Long getId();

    String getName();

    String getPath();
}
