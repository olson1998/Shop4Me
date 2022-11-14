package com.shop4me.core.adapter.inbound.admin;

import com.shop4me.core.adapter.inbound.exception.UnreadableTenantIdentificationException;
import com.shop4me.core.application.dto.productdatastream.Category;
import com.shop4me.core.application.dto.productdatastream.Product;
import com.shop4me.core.adapter.inbound.exception.UnknownApplicationVersionException;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
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

    private final AdminRequestRepository messagingRequestAdminRepository;

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

    @PutMapping(path = "/product/edit")
    public CompletableFuture<RequestProcessingReport> editProduct(@PathVariable("version") String version,
                                                                  @PathVariable("tenant") String id,
                                                                  @RequestBody Map<String, String> productPropertyEditMap){
        return adminRequestRepository(version).editProduct(tenant(id), productPropertyEditMap);
    }

    @PutMapping(path = "/product/edit/categories")
    public CompletableFuture<RequestProcessingReport> editProductsCategories(@PathVariable("version") String version,
                                                                             @PathVariable("tenant") String id,
                                                                             @RequestParam("id") String productId, @RequestBody Long[] categoriesIds){
        return adminRequestRepository(version).editProductsCategories(tenant(id), productId, categoriesIds);
    }

    @PutMapping(path = "/product/edit/imageurl")
    public CompletableFuture<RequestProcessingReport> editProductsImagesUrls(@PathVariable("version") String version,
                                                                             @PathVariable("tenant") String id,
                                                                             @RequestParam("id") String productId,@RequestBody  String[] imageUrlsIds){
        return adminRequestRepository(version).editProductsImageUrls(tenant(id), productId, imageUrlsIds);
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
                return messagingRequestAdminRepository;
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
