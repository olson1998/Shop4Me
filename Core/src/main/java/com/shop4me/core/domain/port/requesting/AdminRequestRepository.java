package com.shop4me.core.domain.port.requesting;

import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface AdminRequestRepository {

    CompletableFuture<RequestProcessingReport> saveProducts(ProductDto[] products);

    CompletableFuture<RequestProcessingReport> saveCategories(CategoryDto[] categories);

    CompletableFuture<RequestProcessingReport> deleteProduct(ProductDto product);

    CompletableFuture<RequestProcessingReport> editProduct(Map<String, String> productPropertyNewValueMap);

    CompletableFuture<RequestProcessingReport> editProductsCategories(String productId, Long[] categoriesIds);

    CompletableFuture<RequestProcessingReport> editProductsImageUrls(String productId, String[] imageUrlsIds);
}
