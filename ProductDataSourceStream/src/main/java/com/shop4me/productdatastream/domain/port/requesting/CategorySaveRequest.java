package com.shop4me.productdatastream.domain.port.requesting;

import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;

import java.util.Map;

public interface CategorySaveRequest {

    Map<String, CategoryDto> getCategoriesToSaveMap();
}
