package com.shop4me.productdatastream.domain.service.requesting;

import com.shop4me.productdatastream.domain.model.request.product.ProductObtainRequestImpl;
import com.shop4me.productdatastream.domain.port.persisting.repositories.product.*;
import com.shop4me.productdatastream.domain.port.requesting.ProductObtainRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.shop4me.productdatastream.application.request.ProductOperationTestRequest.productObtainRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class Shop4MeProductRequestHandlerTest {

    @Mock
    private ProductObtainingExecutor productObtainingExecutor;

    @Mock
    private ProductSavingExecutor productSavingExecutor;

    @Mock
    private ProductSearchingExecutor productSearchingExecutor;

    @Mock
    private ProductEditingExecutor productEditingExecutor;

    @Mock
    private ProductDeletingExecutor productDeletingExecutor;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    void shouldDelegateTheRequestToProductObtainExecutor(){
        var request = productObtainRequest(1, 2, 3);

        var response = shop4MeProductRequestService().handle(request)
                        .exceptionally(e-> {
                            log.error(e.toString());
                            return null;
                        });

        then(productObtainingExecutor).should()
                .execute(any(ProductObtainRequestImpl.class));

        assertThat(response).isCompleted();
    }

    private Shop4MeProductRequestService shop4MeProductRequestService(){
        return new Shop4MeProductRequestService(
                productObtainingExecutor,
                productSearchingExecutor,
                productSavingExecutor,
                productEditingExecutor,
                productDeletingExecutor
        );
    }
}
