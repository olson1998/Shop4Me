package com.shop4me.core.domain.port.requesting;

import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface AdminRequestRepository {

    CompletableFuture<Map<String, String>> saveProducts(ProductDto[] products);

    CompletableFuture<Map<String, String>> saveCategories(CategoryDto[] categories);
}
