package com.shop4me.core.domain.port.web.datastream;

import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import com.shop4me.core.domain.port.dto.productdatastream.utils.ProductSearchFilterDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface ProductDataStream {

    Mono<List<ProductDto>> requestObtainingProducts(int[] idArray);

    Mono<Map<String, String>> requestSavingProducts(Map<String, ProductDto> productSavingMap);

    Mono<Map<String, Integer>> requestEditingProducts(Map<String, String> editingProductMap);

    Mono<Integer[]> requestSearchingProduct(List<ProductSearchFilterDto> productSearchFilters);

    Mono<Map<String, Integer>> requestDeletingProduct(ProductDto product);

    Mono<Map<String, String>> requestSavingReview(Map<String, ReviewDto> reviewSaveMap);

    Mono<Map<String, Integer>> requestEditingReview(Map<String, String> reviewEditMap);

    Mono<Map<String, Integer>> requestDeletingReview(ReviewDto review);

    Mono<Map<String, String>> requestSavingCategory(Map<String, CategoryDto> categorySaveMap);
}
