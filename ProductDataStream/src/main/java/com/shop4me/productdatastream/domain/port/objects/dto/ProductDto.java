package com.shop4me.productdatastream.domain.port.objects.dto;

import com.shop4me.productdatastream.domain.port.objects.dao.ProductDao;

import java.util.Set;

public interface ProductDto {

    Long getId();

    String getName();

    String getDescription();

    String getCreatingTimestamp();

    Set<CategoryDto> getCategoriesSet();

    Set<ReviewDto> getReviewsSet();

    Set<ImageUrlDto> getImageUrlSet();

    ProductDao toDao();

    void setTenantId(int tenantId);
}
