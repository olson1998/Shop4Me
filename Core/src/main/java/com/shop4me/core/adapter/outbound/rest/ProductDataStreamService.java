package com.shop4me.core.adapter.outbound.rest;

import com.shop4me.core.application.dto.productdatastream.Category;
import com.shop4me.core.application.dto.productdatastream.Product;
import com.shop4me.core.application.dto.productdatastream.Review;
import com.shop4me.core.domain.model.request.utils.ProductSearchFilter;
import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import com.shop4me.core.domain.port.dto.productdatastream.utils.ProductSearchFilterDto;
import com.shop4me.core.domain.port.web.datastream.ProductDataStream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor

@Service
public class ProductDataStreamService implements ProductDataStream {

    private final ProductDataStreamRestService productDataStream;

    @Override
    public Mono<List<ProductDto>> requestObtainingProducts(int tenantId, int[] idArray) {
        return productDataStream.requestObtainingProducts(tenantId, idArray)
                .map(this::createProductDtoList);
    }

    @Override
    public Mono<Map<String, String>> requestSavingProducts(int tenantId, Map<String, ProductDto> productDtoSavingMap) {
        var productSavingMap = createSavingMapFromSavingMapDto(productDtoSavingMap);
        return productDataStream.requestSavingProducts(tenantId, productSavingMap);
    }

    @Override
    public Mono<Map<String, Integer>> requestEditingProducts(int tenantId, Map<String, String> editingProductMap) {
        return productDataStream.requestEditingProducts(tenantId, editingProductMap);
    }

    @Override
    public Mono<Integer[]> requestSearchingProduct(int tenantId, List<ProductSearchFilterDto> productSearchFiltersDtoList) {
        var productSearchFilters = createProductSearchFiltersFromDto(productSearchFiltersDtoList);
        return productDataStream.requestSearchingProduct(tenantId, productSearchFilters);
    }

    @Override
    public Mono<Map<String, Integer>> requestDeletingProduct(int tenantId, ProductDto productDto) {
        var product = Product.fromDto(productDto);
        return productDataStream.requestDeletingProduct(tenantId, product);
    }

    @Override
    public Mono<Map<String, String>> requestSavingReview(int tenantId, Map<String, ReviewDto> reviewDtoSavingMap) {
        var reviewSavingMap = createSavingMapFromReviewDtoSavingMap(reviewDtoSavingMap);
        return productDataStream.requestSavingReview(tenantId, reviewSavingMap);
    }

    @Override
    public Mono<Map<String, Integer>> requestEditingReview(int tenantId, Map<String, String> reviewEditMap) {
        return productDataStream.requestEditingReview(tenantId, reviewEditMap);
    }

    @Override
    public Mono<Map<String, Integer>> requestDeletingReview(int tenantId, ReviewDto reviewDto) {
        var review = Review.fromDto(reviewDto);

        return productDataStream.requestDeletingReview(tenantId, review);
    }

    @Override
    public Mono<Map<String, String>> requestSavingCategory(int tenantId, Map<String, CategoryDto> categoryDtoSaveMap) {
        var categorySaveMap = createSavingMapFromCategorySavingMapDto(categoryDtoSaveMap);
        return productDataStream.requestSavingCategory(tenantId, categorySaveMap);
    }

    private List<ProductDto> createProductDtoList(Product[] products){
        return Arrays.stream(products)
                .map(ProductDto.class::cast)
                .toList();
    }

    private List<ProductSearchFilter> createProductSearchFiltersFromDto(@NonNull List<ProductSearchFilterDto> productSearchFilterDtoList){
        return productSearchFilterDtoList.stream()
                .map(ProductSearchFilter::fromDto)
                .toList();
    }

    private Map<String, Product> createSavingMapFromSavingMapDto(@NonNull Map<String, ProductDto> savingProductsMapDto){
        var productSavingMap = new HashMap<String, Product>();

        savingProductsMapDto.keySet().forEach(correlationId -> {
            var productDto = savingProductsMapDto.get(correlationId);
            productSavingMap.put(correlationId, Product.fromDto(productDto));
        });
        return productSavingMap;
    }

    private Map<String, Category> createSavingMapFromCategorySavingMapDto(@NonNull Map<String, CategoryDto> savingCategoryMapDto){
        var categorySavingMap = new HashMap<String, Category>();

        savingCategoryMapDto.forEach((correlationId, categoryDto) ->{
            var category = Category.fromDto(categoryDto);
            categorySavingMap.put(correlationId, category);
        });

        return categorySavingMap;
    }

    private Map<String, Review> createSavingMapFromReviewDtoSavingMap(@NonNull Map<String, ReviewDto> reviewDtoSavingMap){
        var reviewSavingMap = new HashMap<String, Review>();

        reviewDtoSavingMap.forEach((correlationId, reviewDto)-> {
            var review = Review.fromDto(reviewDto);
            reviewSavingMap.put(correlationId, review);
        });
        return reviewSavingMap;
    }
}
