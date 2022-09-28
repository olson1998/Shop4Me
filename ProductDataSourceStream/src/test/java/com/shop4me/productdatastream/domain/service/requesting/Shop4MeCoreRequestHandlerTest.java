package com.shop4me.productdatastream.domain.service.requesting;

import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.handler.CategoryRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.ProductRequestHandler;
import com.shop4me.productdatastream.domain.port.requesting.handler.ReviewRequestHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static com.shop4me.productdatastream.application.request.CategoryOperationTestRequest.CATEGORY;
import static com.shop4me.productdatastream.application.request.ProductOperationTestRequest.PRODUCT;
import static com.shop4me.productdatastream.application.request.ReviewOperationTestRequest.REVIEW;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class Shop4MeCoreRequestHandlerTest {

    @Mock
    private ProductRequestHandler productRequestHandler;

    @Mock
    private CategoryRequestHandler categoryRequestHandler;

    @Mock
    private ReviewRequestHandler reviewRequestHandler;

    @Mock
    private CoreRequest coreRequest;

    @Test
    void shouldDelegateProductOperationRequestHandlerToExecuteRequest(){
        given(coreRequest.getEntity())
                .willReturn(PRODUCT);
        given(productRequestHandler.handle(coreRequest))
                .willReturn(CompletableFuture.completedFuture("test"));

        shop4MeCoreRequestService()
                .handle(coreRequest);

        then(productRequestHandler).should().handle(coreRequest);
    }

    @Test
    void shouldDelegateCategoryOperationRequestHandlerToExecuteRequest(){
        given(coreRequest.getEntity())
                .willReturn(CATEGORY);
        given(categoryRequestHandler.handle(coreRequest))
                .willReturn(CompletableFuture.completedFuture("test"));

        shop4MeCoreRequestService()
                .handle(coreRequest);

        then(categoryRequestHandler).should().handle(coreRequest);
    }

    @Test
    void shouldDelegateReviewOperationRequestHandlerToExecuteRequest(){
        given(coreRequest.getEntity())
                .willReturn(REVIEW);
        given(reviewRequestHandler.handle(coreRequest))
                .willReturn(CompletableFuture.completedFuture("test"));

        shop4MeCoreRequestService()
                .handle(coreRequest);

        then(reviewRequestHandler).should().handle(coreRequest);
    }

    private Shop4MeCoreRequestService shop4MeCoreRequestService(){
        return new Shop4MeCoreRequestService(
                productRequestHandler,
                categoryRequestHandler,
                reviewRequestHandler
        );
    }
}
