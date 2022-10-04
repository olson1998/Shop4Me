package com.shop4me.core.adapter.inbound;

import com.shop4me.core.application.dto.product_data_stream.Review;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import com.shop4me.core.domain.port.requesting.CustomerRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@CrossOrigin

@Async
@RestController

@AllArgsConstructor

@RequestMapping("/customer")
public class CustomerServiceRestController {

    private final CustomerRequestRepository customerRequestRepository;

    @PostMapping(path = "/review/save")
    public CompletableFuture<RequestProcessingReport> requestSavingReview(@RequestBody Review[] reviewsArray){
        return customerRequestRepository
                .saveCustomerReview(List.of(reviewsArray));
    }
}
