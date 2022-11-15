package com.shop4me.core.adapter.inbound.rest;

import com.shop4me.core.adapter.inbound.rest.exception.UnreadableTenantIdentificationException;
import com.shop4me.core.application.dto.productdatastream.Category;
import com.shop4me.core.application.dto.productdatastream.Product;
import com.shop4me.core.adapter.inbound.rest.exception.UnknownApplicationVersionException;
import com.shop4me.core.application.dto.productdatastream.SearchFilter;
import com.shop4me.core.application.dto.utils.EntityRelationChange;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import com.shop4me.core.domain.port.requesting.AdminMessagingRequestRepository;
import com.shop4me.core.domain.port.requesting.AdminRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@CrossOrigin

@Async
@RestController

@RequiredArgsConstructor

@RequestMapping("/{version}/{tenant}/admin")
public class AdminProductDataStreamController {

    private final AdminRequestRepository restApiAdminRepository;

    private final AdminMessagingRequestRepository adminMessagingRequestRepository;

    @Value("${component.version.rest}")
    private String restCallApplicationVersion;

    @Value("${component.version.messaging}")
    private String messageRequestApplicationVersion;

    @PostMapping(path = "/product/save")
    public CompletableFuture<RequestProcessingReport> saveProducts(@PathVariable("version") String version,
                                                                   @PathVariable("tenant") String id,
                                                                   @RequestBody Product[] products){
        return adminRequestRepository(version).saveProducts(tenant(id), products);
    }

    @PostMapping(path = "/product/obtain")
    public CompletableFuture<ProductDto[]> searchAndObtainProducts(@PathVariable("version") String version,
                                                                   @PathVariable("tenant") String id,
                                                                   @RequestBody SearchFilter[] searchFilters){
        return adminRequestRepository(version).searchAndObtainProducts(tenant(id), searchFilters);
    }

    @PutMapping(path = "/product/edit")
    public CompletableFuture<RequestProcessingReport> editProduct(@PathVariable("version") String version,
                                                                  @PathVariable("tenant") String id,
                                                                  @RequestBody Map<String, String> productPropertyEditMap){
        return adminRequestRepository(version).editProduct(tenant(id), productPropertyEditMap);
    }

    @PutMapping(path = "/product/edit/categories")
    public CompletableFuture<RequestProcessingReport> editProductsCategories(@PathVariable("version") String version,
                                                                             @PathVariable("tenant") String id,
                                                                             @RequestParam("id") String productId, @RequestBody EntityRelationChange[] relationEdits){
        return adminRequestRepository(version).editProductsCategories(tenant(id), productId, relationEdits);
    }

    @PutMapping(path = "/product/edit/imageurl")
    public CompletableFuture<RequestProcessingReport> editProductsImagesUrls(@PathVariable("version") String version,
                                                                             @PathVariable("tenant") String id,
                                                                             @RequestParam("id") String productId,@RequestBody EntityRelationChange[] relationEdits){
        return adminRequestRepository(version).editProductsImageUrls(tenant(id), productId, relationEdits);
    }

    @DeleteMapping(path = "/product/delete")
    public CompletableFuture<RequestProcessingReport> deleteProduct(@PathVariable("version") String version,
                                                                    @PathVariable("tenant") String id,
                                                                    @RequestBody Product product){
        return adminRequestRepository(version).deleteProduct(tenant(id), product);
    }

    @PostMapping(path = "/category/save")
    public CompletableFuture<RequestProcessingReport> saveCategories(@PathVariable("version") String version,
                                                                     @PathVariable("tenant") String id,
                                                                     @RequestBody Category[] categories){
        return adminRequestRepository(version).saveCategories(tenant(id), categories);
    }

    private AdminRequestRepository adminRequestRepository(String version){
        if(version != null){
            if(version.equals(messageRequestApplicationVersion)){
                return adminMessagingRequestRepository;
            }else if (version.equals(restCallApplicationVersion)){
                return restApiAdminRepository;
            }else {
                throw new UnknownApplicationVersionException(version);
            }
        }else {
            throw new UnknownError();
        }
    }

    private int tenant(String id){
        try{
            return Integer.parseInt(id);
        }catch (NumberFormatException e){
            throw new UnreadableTenantIdentificationException();
        }
    }
}
