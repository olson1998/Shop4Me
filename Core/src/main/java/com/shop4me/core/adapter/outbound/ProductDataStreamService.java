package com.shop4me.core.adapter.outbound;

import com.shop4me.core.application.dto.product_data_stream.Category;
import com.shop4me.core.application.dto.product_data_stream.Product;
import com.shop4me.core.application.dto.product_data_stream.Review;
import com.shop4me.core.domain.model.request.tool.ProductSearchFilter;
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
    public Mono<List<ProductDto>> requestObtainingProducts(int[] idArray) {
        return productDataStream.requestObtainingProducts(idArray)
                .map(this::createProductDtoList);
    }

    @Override
    public Mono<Map<String, String>> requestSavingProducts(Map<String, ProductDto> productDtoSavingMap) {
        var productSavingMap = createSavingMapFromSavingMapDto(productDtoSavingMap);
        return productDataStream.requestSavingProducts(productSavingMap);
    }

    @Override
    public Mono<Map<String, Integer>> requestEditingProducts(Map<String, String> editingProductMap) {
        return productDataStream.requestEditingProducts(editingProductMap);
    }

    @Override
    public Mono<Integer[]> requestSearchingProduct(List<ProductSearchFilterDto> productSearchFiltersDtoList) {
        var productSearchFilters = createProductSearchFiltersFromDto(productSearchFiltersDtoList);
        return productDataStream.requestSearchingProduct(productSearchFilters);
    }

    @Override
    public Mono<Map<String, Integer>> requestDeletingProduct(ProductDto productDto) {
        var product = Product.fromDto(productDto);
        return productDataStream.requestDeletingProduct(product);
    }

    @Override
    public Mono<Map<String, String>> requestSavingReview(Map<String, ReviewDto> reviewDtoSavingMap) {
        var reviewSavingMap = createSavingMapFromReviewDtoSavingMap(reviewDtoSavingMap);
        return productDataStream.requestSavingReview(reviewSavingMap);
    }

    @Override
    public Mono<Map<String, Integer>> requestEditingReview(Map<String, String> reviewEditMap) {
        return productDataStream.requestEditingReview(reviewEditMap);
    }

    @Override
    public Mono<Map<String, Integer>> requestDeletingReview(ReviewDto reviewDto) {
        var review = Review.fromDto(reviewDto);

        return productDataStream.requestDeletingReview(review);
    }

    @Override
    public Mono<Map<String, String>> requestSavingCategory(Map<String, CategoryDto> categoryDtoSaveMap) {
        var categorySaveMap = createSavingMapFromCategorySavingMapDto(categoryDtoSaveMap);
        return productDataStream.requestSavingCategory(categorySaveMap);
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
