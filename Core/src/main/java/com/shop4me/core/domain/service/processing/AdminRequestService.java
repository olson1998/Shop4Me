package com.shop4me.core.domain.service.processing;

import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.requesting.AdminRequestRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.CategoryRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ProductRepository;
import com.shop4me.core.domain.service.processing.utils.RequestProcessingExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j

@AllArgsConstructor
public class AdminRequestService implements AdminRequestRepository {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public CompletableFuture<Map<String, String>> saveProducts(ProductDto[] products){
        log.info("ADMIN ip: {}, REQUESTED SAVING: {}", "?", products);
        return productRepository.requestSavingProducts(products)
                .toFuture()
                .exceptionally(RequestProcessingExceptionHandler::returnEmptyMap);
    }

    @Override
    public CompletableFuture<Map<String, String>> saveCategories(CategoryDto[] categories){
        log.info("ADMIN ip: {}, REQUESTED SAVING: {}", "?", categories);
        return categoryRepository.saveCategories(categories)
                .toFuture()
                .exceptionally(RequestProcessingExceptionHandler::returnEmptyMap);
    }
}
