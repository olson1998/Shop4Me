package com.shop4me.core.domain.port.requesting;

import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.productdatastream.utils.ProductSearchFilterDto;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import com.shop4me.core.domain.port.dto.utils.RelationEdit;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface AdminRequestRepository {

    CompletableFuture<RequestProcessingReport> saveProducts(int tenantId, ProductDto[] products);

    CompletableFuture<ProductDto[]> searchAndObtainProducts(int tenantId, ProductSearchFilterDto[] searchFilters);

    CompletableFuture<RequestProcessingReport> saveCategories(int tenantId, CategoryDto[] categories);

    CompletableFuture<RequestProcessingReport> deleteProduct(int tenantId, ProductDto product);

    CompletableFuture<RequestProcessingReport> editProduct(int tenantId, Map<String, String> productPropertyNewValueMap);

    CompletableFuture<RequestProcessingReport> editProductsCategories(int tenantId, String productId, RelationEdit[] relationEdits);

    CompletableFuture<RequestProcessingReport> editProductsImageUrls(int tenantId, String productId, RelationEdit[] relationEdits);
}
