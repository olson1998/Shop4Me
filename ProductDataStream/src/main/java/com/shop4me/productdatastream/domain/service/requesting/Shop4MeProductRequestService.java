package com.shop4me.productdatastream.domain.service.requesting;

import com.shop4me.productdatastream.domain.model.exception.OperationNotMatchingWithEntityException;
import com.shop4me.productdatastream.domain.model.request.enumset.Operation;
import com.shop4me.productdatastream.domain.model.request.product.*;
import com.shop4me.productdatastream.domain.port.persisting.repositories.product.*;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.handler.ProductRequestHandler;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor

public class Shop4MeProductRequestService implements ProductRequestHandler {

    private final ProductObtainingExecutor productObtainingService;

    private final ProductSearchingExecutor productSearchingService;

    private final ProductSavingExecutor productSavingService;

    private final ProductEditingExecutor productEditingService;

    private final ProductDeletingExecutor productDeletingService;

    @Override
    public CompletableFuture<Object> handle(CoreRequest request){
        return CompletableFuture.supplyAsync(()-> delegateRequest(request));
    }

    private Object delegateRequest(@NotNull CoreRequest  request){
        switch (Operation.valueOf(request.getOperation())){
            case OBTAIN -> {
                return productObtainingService.execute(
                        ProductObtainRequestImpl.fromCoreRequest(request)
                );
            }
            case SEARCH -> {
                return productSearchingService.execute(
                        ProductSearchRequestImpl.fromCoreRequest(request)
                );
            }
            case SAVE -> {
                return productSavingService.execute(
                        ProductSaveRequestImpl.fromCoreRequest(request)
                );
            }
            case EDIT -> {
                return productEditingService.execute(
                        ProductEditRequestImpl.fromCoreRequest(request)
                );
            }
            case DELETE -> {
                return productDeletingService.execute(
                        ProductDeleteRequestImpl.fromCoreRequest(request)
                );
            }
        }
        throw new OperationNotMatchingWithEntityException(request.getEntity(), request.getOperation());
    }

}
