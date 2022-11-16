package com.shop4me.productdatastream.domain.service.requesting;

import com.shop4me.productdatastream.domain.model.request.enumset.Operation;
import com.shop4me.productdatastream.domain.port.requesting.handler.ProductRequestHandler;
import com.shop4me.productdatastream.domain.model.exception.OperationNotMatchingWithEntityException;
import com.shop4me.productdatastream.domain.model.request.product.*;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.persisting.product.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor

public class Shop4MeProductRequestService implements ProductRequestHandler {

    private final ProductObtainingExecutor productObtainingService;

    private final ProductSearchingExecutor productSearchingService;

    private final ProductSavingExecutor productSavingService;

    private final ProductEditingExecutor productEditingService;

    private final ProductDeletingExecutor productDeletingService;

    @Override
    public Object handle(InboundMsg inboundMsg){
        return delegateRequest(inboundMsg);
    }

    private Object delegateRequest(@NonNull InboundMsg inboundMsg){
        switch (Operation.valueOf(inboundMsg.getOperation())){
            case OBTAIN -> {
                return productObtainingService.execute(
                        ProductObtainRequestImpl.fromInboundMessage(inboundMsg)
                );
            }
            case SEARCH -> {
                return productSearchingService.execute(
                        ProductSearchRequestImpl.fromInboundMessage(inboundMsg)
                );
            }
            case SEARCH_MANY -> {
                return productSearchingService.execute(
                        ProductMultiSearchRequestImpl.fromInboundMessage(inboundMsg)
                );
            }
            case SAVE -> {
                return productSavingService.execute(
                        ProductSaveRequestImpl.fromInboundMessage(inboundMsg)
                );
            }
            case EDIT -> {
                return productEditingService.execute(
                        ProductEditRequestImpl.fromInboundMessage(inboundMsg)
                );
            }
            case DELETE -> {
                return productDeletingService.execute(
                        ProductDeleteRequestImpl.fromInboundMessage(inboundMsg)
                );
            }
        }
        throw new OperationNotMatchingWithEntityException("PRODUCT", inboundMsg.getOperation());
    }

}
