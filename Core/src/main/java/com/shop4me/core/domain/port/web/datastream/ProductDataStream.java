package com.shop4me.core.domain.port.web.datastream;

import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import com.shop4me.core.domain.port.dto.productdatastream.utils.ProductSearchFilterDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface ProductDataStream {

    Mono<List<ProductDto>> requestObtainingProducts(int tenantId, int[] idArray);

    Mono<Map<String, String>> requestSavingProducts(int tenantId, Map<String, ProductDto> productSavingMap);

    Mono<Map<String, Integer>> requestEditingProducts(int tenantId, Map<String, String> editingProductMap);

    Mono<Integer[]> requestSearchingProduct(int tenantId, List<ProductSearchFilterDto> productSearchFilters);

    Mono<Map<String, Integer>> requestDeletingProduct(int tenantId, ProductDto product);

    Mono<Map<String, String>> requestSavingReview(int tenantId, Map<String, ReviewDto> reviewSaveMap);

    Mono<Map<String, Integer>> requestEditingReview(int tenantId, Map<String, String> reviewEditMap);

    Mono<Map<String, Integer>> requestDeletingReview(int tenantId, ReviewDto review);

    Mono<Map<String, String>> requestSavingCategory(int tenantId, Map<String, CategoryDto> categorySaveMap);
}
