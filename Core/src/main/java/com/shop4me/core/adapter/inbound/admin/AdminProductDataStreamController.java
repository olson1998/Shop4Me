package com.shop4me.core.adapter.inbound.admin;

import com.shop4me.core.application.dto.product_data_stream.Category;
import com.shop4me.core.application.dto.product_data_stream.Product;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import com.shop4me.core.domain.port.requesting.AdminRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@CrossOrigin

@Async
@RestController

@RequiredArgsConstructor

@RequestMapping("/admin")
public class AdminProductDataStreamController {

    private final AdminRequestRepository adminRequestRepository;


    @PostMapping(path = "/product/save")
    public CompletableFuture<RequestProcessingReport> saveProducts(@RequestBody Product[] products){
        return adminRequestRepository.saveProducts(products);
    }

    @PutMapping(path = "/product/edit")
    public CompletableFuture<RequestProcessingReport> editProduct(@RequestBody Map<String, String> productPropertyEditMap){
        return adminRequestRepository.editProduct(productPropertyEditMap);
    }

    @PutMapping(path = "/product/edit/categories")
    public CompletableFuture<RequestProcessingReport> editProductsCategories(@RequestParam("id") String productId, @RequestBody Long[] categoriesIds){
        return adminRequestRepository.editProductsCategories(productId, categoriesIds);
    }

    @PutMapping(path = "/product/edit/imageurl")
    public CompletableFuture<RequestProcessingReport> editProductsImagesUrls(@RequestParam("id") String productId,@RequestBody  String[] imageUrlsIds){
        return adminRequestRepository.editProductsImageUrls(productId, imageUrlsIds);
    }

    @DeleteMapping(path = "/product/delete")
    public CompletableFuture<RequestProcessingReport> deleteProduct(@RequestBody Product product){
        return adminRequestRepository.deleteProduct(product);
    }

    @PostMapping(path = "/category/save")
    public CompletableFuture<RequestProcessingReport> saveCategories(@RequestBody Category[] categories){
        return adminRequestRepository.saveCategories(categories);
    }
}
