package com.shop4me.productdatastream.domain.service.requesting;

import com.shop4me.productdatastream.domain.model.data.dto.Product;
import com.shop4me.productdatastream.domain.model.request.product.tools.ProductSearchFilter;
import com.shop4me.productdatastream.domain.port.persisting.repositories.product.*;
import com.shop4me.productdatastream.domain.port.requesting.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.shop4me.productdatastream.application.request.ProductOperationTestRequest.*;
import static com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.product.ProductProperty.ID;
import static com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.product.ProductProperty.NAME;
import static com.shop4me.productdatastream.domain.model.request.enumset.Operator.LIKE;
import static java.util.Map.entry;
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

    private static final long[] PRODUCT_OBTAIN_REQUEST_PAYLOAD = {1L, 2L, 3L};

    private static final ProductSearchFilter[] PRODUCT_SEARCH_REQUEST_PAYLOAD = {
            new ProductSearchFilter(NAME, LIKE, "test")
    };

    private static final Map<String, String> PRODUCT_EDIT_REQUEST_PAYLOAD = Map.ofEntries(
            entry(ID.name(), "1"),
            entry(NAME.name(), "test")
    );

    private static final Product PRODUCT_DELETE_REQUEST_PAYLOAD = new Product(1L, "test", null, null, null, null, null);

    private static final Map<String, Product> PRODUCT_SAVE_REQUEST_PAYLOAD = Map.ofEntries(
            entry(UUID.randomUUID().toString(), new Product(null, "test", null, null, null, null, null))
    );

    @Test
    void shouldDelegateTheRequestToProductObtainExecutor() throws ExecutionException, InterruptedException {
        shop4MeProductRequestService().handle(productObtainCoreRequest(PRODUCT_OBTAIN_REQUEST_PAYLOAD)).get();

        then(productObtainingExecutor).should().execute(any(ProductObtainRequest.class));
    }

    @Test
    void shouldDelegateTheRequestToProductSearchExecutor() throws ExecutionException, InterruptedException {
        shop4MeProductRequestService().handle(productSearchCoreRequest(PRODUCT_SEARCH_REQUEST_PAYLOAD))
                .get();

        then(productSearchingExecutor).should().execute(any(ProductSearchRequest.class));
    }

    @Test
    void shouldDelegateTheRequestToProductEditExecutor() throws ExecutionException, InterruptedException {
        shop4MeProductRequestService().handle(productEditCoreRequest(PRODUCT_EDIT_REQUEST_PAYLOAD))
                .get();

        then(productEditingExecutor).should().execute(any(ProductEditRequest.class));
    }

    @Test
    void shouldDelegateTheRequestToProductDeleteExecutor() throws ExecutionException, InterruptedException {
        shop4MeProductRequestService().handle(productDeleteCoreRequest(PRODUCT_DELETE_REQUEST_PAYLOAD))
                .get();

        then(productDeletingExecutor).should().execute(any(ProductDeleteRequest.class));
    }

    @Test
    void shouldDelegateRequestToProductSaveExecutor() throws ExecutionException, InterruptedException {
        shop4MeProductRequestService().handle(productSaveCoreRequest(PRODUCT_SAVE_REQUEST_PAYLOAD))
                .get();

        then(productSavingExecutor).should().execute(any(ProductSaveRequest.class));
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
