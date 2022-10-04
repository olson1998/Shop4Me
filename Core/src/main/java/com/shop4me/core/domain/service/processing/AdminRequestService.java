package com.shop4me.core.domain.service.processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.model.exception.ProductIdNotANumberException;
import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import com.shop4me.core.domain.port.requesting.AdminRequestRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.CategoryRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ProductRepository;
import com.shop4me.core.domain.service.processing.report.AffectedRowsReportingServing;
import com.shop4me.core.domain.service.processing.report.ErrorReportingService;
import com.shop4me.core.domain.service.processing.report.SavingReportingService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.Map.entry;

@Slf4j

@AllArgsConstructor
public class AdminRequestService implements AdminRequestRepository {

    private final ObjectMapper mapper;

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public CompletableFuture<RequestProcessingReport> saveProducts(ProductDto[] products){
        log.info("ADMIN ip: {}, REQUESTED SAVING: {}", "?", products);
        var productSaveMap = createSavingProductMap(products);
        return productRepository.requestSavingProducts(productSaveMap)
                .toFuture()
                .thenApply(response -> SavingReportingService.productSavingReport(productSaveMap, response))
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> saveCategories(CategoryDto[] categories){
        log.info("ADMIN ip: {}, REQUESTED SAVING: {}", "?", categories);
        var categoriesSaveMap = createSaveCategoryMap(categories);
        return categoryRepository.saveCategories(categoriesSaveMap)
                .toFuture()
                .thenApply(response -> SavingReportingService.categorySavingReport(categoriesSaveMap, response))
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> deleteProduct(ProductDto product){
        log.info("ADMIN ip: {}, REQUESTED DELETING: {}", "?", product);
        return productRepository.requestDeletingProduct(product)
                .toFuture()
                .thenApply(AffectedRowsReportingServing::affectedRowsReport)
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProduct(Map<String, String> productPropertyNewValueMap){
        log.info("ADMIN ip: {}, REQUESTED EDITING: {}", "?", productPropertyNewValueMap);
        return productRepository.requestEditingProduct(productPropertyNewValueMap)
                .toFuture()
                .thenApply(AffectedRowsReportingServing::affectedRowsReport)
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProductsCategories(String productId, Long[] categoriesIds) {
        var productsCategoriesEditMap = createProductsCategoryEditMap(productId, categoriesIds);
        return productRepository.requestEditingProduct(productsCategoriesEditMap)
                .toFuture()
                .thenApply(AffectedRowsReportingServing::affectedRowsReport)
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProductsImageUrls(String productId, String[] imageUrlsIds) {
        var productsImageUrlsEditMap = createProductsImageUrlsEditMap(productId, imageUrlsIds);
        return productRepository.requestEditingProduct(productsImageUrlsEditMap)
                .toFuture()
                .thenApply(AffectedRowsReportingServing::affectedRowsReport)
                .exceptionally(ErrorReportingService::error);
    }

    @SneakyThrows
    private Map<String, String> createProductsCategoryEditMap(@NonNull String productId, @NonNull Long[] categoriesIds){
        checkProductId(productId);
        var json = mapper.writeValueAsString(categoriesIds);
        return Map.ofEntries(
                entry("ID", productId),
                entry("CATEGORY", json)
        );
    }

    @SneakyThrows
    private Map<String, String> createProductsImageUrlsEditMap(@NonNull String productId, @NonNull String[] imageUrlsIds){
        var json = mapper.writeValueAsString(imageUrlsIds);
        return Map.ofEntries(
                entry("ID", productId),
                entry("PHOTO_URL", json)
        );
    }

    private Map<String, CategoryDto> createSaveCategoryMap(CategoryDto[] categories){
        var saveMap = new HashMap<String, CategoryDto>();
        Arrays.stream(categories).forEach(category ->{
            var absolutePath = category.getAbsolutePath();
            saveMap.put(absolutePath, category);
        });
        return saveMap;
    }

    private Map<String, ProductDto> createSavingProductMap(@NonNull ProductDto[] products){
        var editMap = new HashMap<String, ProductDto>();
        Arrays.stream(products).forEach(product -> editMap.put(UUID.randomUUID().toString(), product));
        return editMap;
    }

    private void checkProductId(String productId){
        if(!StringUtils.isAlphanumeric(productId)){
            throw new ProductIdNotANumberException(productId);
        }
    }
}
