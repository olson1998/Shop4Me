package com.shop4me.productdatastream.domain.service.requesting;

import com.shop4me.productdatastream.domain.model.exception.OperationNotMatchingWithEntityException;
import com.shop4me.productdatastream.domain.model.request.enumset.Operation;
import com.shop4me.productdatastream.domain.model.request.review.ReviewDeleteRequestImpl;
import com.shop4me.productdatastream.domain.model.request.review.ReviewEditRequestImpl;
import com.shop4me.productdatastream.domain.model.request.review.ReviewSaveRequestImpl;
import com.shop4me.productdatastream.domain.port.persisting.repositories.review.ReviewDeletingExecutor;
import com.shop4me.productdatastream.domain.port.persisting.repositories.review.ReviewEditingExecutor;
import com.shop4me.productdatastream.domain.port.persisting.repositories.review.ReviewSavingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.handler.ReviewRequestHandler;
import lombok.AllArgsConstructor;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor

public class Shop4MeReviewRequestService implements ReviewRequestHandler {

    private final ReviewSavingExecutor reviewSavingService;

    private final ReviewEditingExecutor reviewEditingService;

    private final ReviewDeletingExecutor reviewDeletingService;

    @Override
    public CompletableFuture<Object> handle(CoreRequest request){
        return CompletableFuture.supplyAsync(()-> delegateRequest(request));
    }

    private Object delegateRequest(CoreRequest  request){
        switch (Operation.valueOf(request.getOperation())){
            case SAVE -> {
                return reviewSavingService.execute(
                        ReviewSaveRequestImpl.fromCoreRequest(request)
                );
            }
            case EDIT -> {
                return reviewEditingService.execute(
                        ReviewEditRequestImpl.fromCoreRequest(request)
                );
            }
            case DELETE ->{
                return reviewDeletingService.execute(
                        ReviewDeleteRequestImpl.fromCoreRequest(request)
                );
            }
        }
        throw new OperationNotMatchingWithEntityException(request.getEntity(), request.getOperation());
    }
}
