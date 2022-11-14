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
    public CompletableFuture<RequestProcessingReport> saveProducts(int tenantId, ProductDto[] products){
        var productSaveMap = createSavingProductMap(products);
        log.info("REQUESTED SAVING: {}", productSaveMap);
        return productRepository.requestSavingProducts(tenantId, productSaveMap)
                .toFuture()
                .thenApply(response -> SavingReportingService.write(response, productSaveMap.keySet()))
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> saveCategories(int tenantId, CategoryDto[] categories){
        var categoriesSaveMap = createSaveCategoryMap(categories);
        log.info("REQUESTED SAVING: {}", categoriesSaveMap);
        return categoryRepository.saveCategories(tenantId, categoriesSaveMap)
                .toFuture()
                .thenApply(response -> SavingReportingService.write(response, categoriesSaveMap.keySet()))
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> deleteProduct(int tenantId, ProductDto product){
        log.info("REQUESTED DELETING: {}",  product);
        return productRepository.requestDeletingProduct(tenantId, product)
                .toFuture()
                .thenApply(AffectedRowsReportingServing::affectedRowsReport)
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProduct(int tenantId, Map<String, String> productPropertyNewValueMap){
        log.info("REQUESTED EDITING: {}", productPropertyNewValueMap);
        return productRepository.requestEditingProduct(tenantId, productPropertyNewValueMap)
                .toFuture()
                .thenApply(AffectedRowsReportingServing::affectedRowsReport)
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProductsCategories(int tenantId, String productId, Long[] categoriesIds) {
        log.info("REQUESTED EDITING PRODUCT: '{}' NEW CATEGORIES: {}", productId, categoriesIds);
        var productsCategoriesEditMap = createProductsCategoryEditMap(productId, categoriesIds);
        return productRepository.requestEditingProduct(tenantId, productsCategoriesEditMap)
                .toFuture()
                .thenApply(AffectedRowsReportingServing::affectedRowsReport)
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProductsImageUrls(int tenantId, String productId, String[] imageUrlsIds) {
        log.info("REQUESTED EDITING PRODUCT: '{}' NEW IMAGE URLS: {}", productId, imageUrlsIds);
        var productsImageUrlsEditMap = createProductsImageUrlsEditMap(productId, imageUrlsIds);
        return productRepository.requestEditingProduct(tenantId, productsImageUrlsEditMap)
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

    private Map<String, CategoryDto> createSaveCategoryMap(@NonNull CategoryDto[] categories){
        var saveMap = new HashMap<String, CategoryDto>();
        Arrays.stream(categories).forEach(category ->{
            var absolutePath = category.getAbsolutePath();
            saveMap.put(absolutePath, category);
        });
        return saveMap;
    }

    private Map<String, ProductDto> createSavingProductMap(@NonNull ProductDto[] products){
        var editMap = new HashMap<String, ProductDto>();
        Arrays.stream(products)
                .forEach(product -> editMap.put(UUID.randomUUID().toString(), product));
        return editMap;
    }

    private void checkProductId(String productId){
        if(!StringUtils.isAlphanumeric(productId)){
            throw new ProductIdNotANumberException(productId);
        }
    }
}
