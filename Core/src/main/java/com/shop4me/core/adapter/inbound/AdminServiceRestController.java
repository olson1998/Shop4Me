package com.shop4me.core.adapter.inbound;

import com.shop4me.core.application.dto.product_data_stream.Category;
import com.shop4me.core.application.dto.product_data_stream.Product;
import com.shop4me.core.domain.port.requesting.AdminRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@CrossOrigin

@Async
@RestController

@AllArgsConstructor

@RequestMapping("/admin")
public class AdminServiceRestController {

    private final AdminRequestRepository adminRequestRepository;


    @PostMapping(path = "/product/save")
    public CompletableFuture<Map<String, String>> saveProducts(@RequestBody Product[] products){
        return adminRequestRepository.saveProducts(products);
    }

    @PostMapping(path = "/category/save")
    public CompletableFuture<Map<String, String>> saveCategories(@RequestBody Category[] categories){
        return adminRequestRepository.saveCategories(categories);
    }
}
