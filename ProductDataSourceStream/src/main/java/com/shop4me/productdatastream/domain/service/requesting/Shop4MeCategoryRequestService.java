package com.shop4me.productdatastream.domain.service.requesting;

import com.shop4me.productdatastream.domain.model.exception.OperationNotMatchingWithEntityException;
import com.shop4me.productdatastream.domain.model.request.category.CategoryObtainRequestImpl;
import com.shop4me.productdatastream.domain.model.request.category.CategorySaveRequestImpl;
import com.shop4me.productdatastream.domain.model.request.enumset.Operation;
import com.shop4me.productdatastream.domain.port.persisting.repositories.category.CategoryObtainExecutor;
import com.shop4me.productdatastream.domain.port.persisting.repositories.category.CategorySaveExecutor;
import com.shop4me.productdatastream.domain.port.requesting.handler.CategoryRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import lombok.AllArgsConstructor;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor

public class Shop4MeCategoryRequestService implements CategoryRequestHandler {

    private final CategoryObtainExecutor categoryObtainService;

    private final CategorySaveExecutor categorySaveService;

    @Override
    public CompletableFuture<Object> handle(CoreRequest request){
        return CompletableFuture.supplyAsync(()-> delegateRequest(request));
    }

    private Object delegateRequest(CoreRequest  request){
        switch (Operation.valueOf(request.getOperation())){
            case SAVE -> {
                return categorySaveService.execute(
                        CategorySaveRequestImpl.fromCoreRequest(request)
                );
            }
            case OBTAIN -> {
                return categoryObtainService.execute(
                        CategoryObtainRequestImpl.fromCoreRequest(request)
                );
            }
        }
        throw new OperationNotMatchingWithEntityException(request.getEntity(), request.getOperation());
    }
}
