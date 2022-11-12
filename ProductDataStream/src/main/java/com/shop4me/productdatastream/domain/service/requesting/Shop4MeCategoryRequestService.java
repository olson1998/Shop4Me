package com.shop4me.productdatastream.domain.service.requesting;

import com.shop4me.productdatastream.domain.model.request.enumset.Operation;
import com.shop4me.productdatastream.domain.port.requesting.handler.CategoryRequestHandler;
import com.shop4me.productdatastream.domain.model.exception.OperationNotMatchingWithEntityException;
import com.shop4me.productdatastream.domain.model.request.category.CategoryObtainRequestImpl;
import com.shop4me.productdatastream.domain.model.request.category.CategorySaveRequestImpl;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.persisting.category.CategoryObtainExecutor;
import com.shop4me.productdatastream.domain.port.persisting.category.CategorySaveExecutor;
import lombok.AllArgsConstructor;

@AllArgsConstructor

public class Shop4MeCategoryRequestService implements CategoryRequestHandler {

    private final CategoryObtainExecutor categoryObtainService;

    private final CategorySaveExecutor categorySaveService;

    @Override
    public Object handle(InboundMsg inboundMsg){
        return delegateRequest(inboundMsg);
    }

    private Object delegateRequest(InboundMsg inboundMsg){
        switch (Operation.valueOf(inboundMsg.getOperation())){
            case SAVE -> {
                return categorySaveService.execute(
                        CategorySaveRequestImpl.fromInboundMessage(inboundMsg)
                );
            }
            case OBTAIN -> {
                return categoryObtainService.execute(
                        CategoryObtainRequestImpl.fromInboundMessage(inboundMsg)
                );
            }
        }
        throw new OperationNotMatchingWithEntityException("CATEGORY", inboundMsg.getOperation());
    }
}
