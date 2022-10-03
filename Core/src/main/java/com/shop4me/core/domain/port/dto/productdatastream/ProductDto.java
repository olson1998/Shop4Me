package com.shop4me.core.domain.port.dto.productdatastream;

import java.util.List;

public interface ProductDto {

    Long getId();

    String getName();

    String getDescription();

    List<ReviewDto> getReviews();

    List<ImageUrlDto> getImageUrls();

    List<CategoryDto> getCategories();
}
