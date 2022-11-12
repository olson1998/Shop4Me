package com.shop4me.productdatastream.domain.service.requesting;

import com.shop4me.productdatastream.domain.model.exception.OperationNotMatchingWithEntityException;
import com.shop4me.productdatastream.domain.model.request.enumset.Operation;
import com.shop4me.productdatastream.domain.model.request.review.ReviewDeleteRequestImpl;
import com.shop4me.productdatastream.domain.model.request.review.ReviewEditRequestImpl;
import com.shop4me.productdatastream.domain.model.request.review.ReviewSaveRequestImpl;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.persisting.review.ReviewDeletingExecutor;
import com.shop4me.productdatastream.domain.port.persisting.review.ReviewEditingExecutor;
import com.shop4me.productdatastream.domain.port.persisting.review.ReviewSavingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.handler.ReviewRequestHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class Shop4MeReviewRequestService implements ReviewRequestHandler {

    private final ReviewSavingExecutor reviewSavingService;

    private final ReviewEditingExecutor reviewEditingService;

    private final ReviewDeletingExecutor reviewDeletingService;

    @Override
    public Object handle(InboundMsg inboundMsg){
        return delegateRequest(inboundMsg);
    }

    private Object delegateRequest(InboundMsg inboundMsg){
        switch (Operation.valueOf(inboundMsg.getOperation())){
            case SAVE -> {
                return reviewSavingService.execute(
                        ReviewSaveRequestImpl.fromInboundMessage(inboundMsg)
                );
            }
            case EDIT -> {
                return reviewEditingService.execute(
                        ReviewEditRequestImpl.fromInboundMessage(inboundMsg)
                );
            }
            case DELETE ->{
                return reviewDeletingService.execute(
                        ReviewDeleteRequestImpl.fromInboundMessage(inboundMsg)
                );
            }
        }
        throw new OperationNotMatchingWithEntityException("REVIEW", inboundMsg.getOperation());
    }
}
