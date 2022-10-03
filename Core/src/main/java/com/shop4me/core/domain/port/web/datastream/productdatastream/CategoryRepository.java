package com.shop4me.core.domain.port.web.datastream.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CategoryRepository {

    Mono<Map<String, String>> saveCategories(CategoryDto[] categories);
}
