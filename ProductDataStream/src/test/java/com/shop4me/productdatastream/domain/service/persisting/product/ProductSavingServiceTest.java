package com.shop4me.productdatastream.domain.service.persisting.product;

import com.shop4me.productdatastream.domain.model.dao.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.model.dto.Product;
import com.shop4me.productdatastream.domain.model.request.product.ProductRequestGenerator;
import com.shop4me.productdatastream.domain.port.requesting.ProductSaveRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import java.util.Map;
import java.util.UUID;

import static com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus.FAILURE;
import static com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus.SUCCESS;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductSavingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Captor
    private ArgumentCaptor<ProductEntity> productEntityArgumentCaptor;

    private static final int TEST_TENANT_ID = 1;

    private static final String PRODUCT_1_CORRELATION_ID= UUID.randomUUID().toString();

    private static final String PRODUCT_2_CORRELATION_ID= UUID.randomUUID().toString();

    private static final String PRODUCT_3_CORRELATION_ID= UUID.randomUUID().toString();

    private static final Product TEST_PRODUCT_1 = new Product("test-product-1", null);

    private static final Product TEST_PRODUCT_2 = new Product("test-product-2", null);

    private static final Product TEST_PRODUCT_3 = new Product("test-product-3", null);

    private static final Map<String, Product> PRODUCT_SAVING_MAP = Map.ofEntries(
            entry(PRODUCT_1_CORRELATION_ID, TEST_PRODUCT_1),
            entry(PRODUCT_2_CORRELATION_ID, TEST_PRODUCT_2),
            entry(PRODUCT_3_CORRELATION_ID, TEST_PRODUCT_3)
    );

    private static final ProductSaveRequest TEST_PRODUCT_SAVE_REQUEST =
            ProductRequestGenerator.productSaveRequest(TEST_TENANT_ID, PRODUCT_SAVING_MAP);

    @Test
    void shouldPersistProductDaoCreatedFromRequest(){
        var qty = PRODUCT_SAVING_MAP.size();

        productSavingService().execute(TEST_PRODUCT_SAVE_REQUEST);

        then(entityManager).should(times(qty)).persist(any(ProductEntity.class));
    }

    @Test
    void shouldPersistProductWithGivenTenantID(){
        var qty = PRODUCT_SAVING_MAP.size();

        productSavingService().execute(TEST_PRODUCT_SAVE_REQUEST);

        then(entityManager).should(times(qty)).persist(productEntityArgumentCaptor.capture());

        var tenantIdList = productEntityArgumentCaptor.getAllValues().stream()
                .map(ProductEntity::getTenantId)
                .toList();

        assertThat(tenantIdList).allMatch(id-> id == TEST_TENANT_ID);
    }

    @Test
    void shouldReturnMapOfCorrelationIdAndSuccessIfSuccessfullySaveEntity(){
        var resultMap = productSavingService().execute(TEST_PRODUCT_SAVE_REQUEST);

        assertThat(resultMap.values()).allMatch(status-> status.equals(SUCCESS.name()));
    }

    @Test
    void shouldReturnMapOfCorrelationIdAndFailureIfFailedToSaveEntity(){
        doThrow(TransactionRequiredException.class).when(entityManager).persist(any(ProductEntity.class));

        var resultMap = productSavingService().execute(TEST_PRODUCT_SAVE_REQUEST);

        assertThat(resultMap.values()).allMatch(status-> status.equals(FAILURE.name()));
    }

    private ProductSavingService productSavingService(){
        return new ProductSavingService(entityManager);
    }
}
