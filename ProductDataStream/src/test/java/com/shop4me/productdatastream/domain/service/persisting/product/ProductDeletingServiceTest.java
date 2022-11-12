package com.shop4me.productdatastream.domain.service.persisting.product;

import com.shop4me.productdatastream.domain.model.request.product.ProductRequestGenerator;
import com.shop4me.productdatastream.domain.port.requesting.ProductDeleteRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductDeletingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query productDeleteQuery;

    private static final int TEST_TENANT_ID = 1;

    private static final long[] TEST_PRODUCTS_IDS = {4L, 10L, 15L};

    private final ProductDeleteRequest TEST_PRODUCT_DELETE_REQUEST =
            ProductRequestGenerator.productDeleteRequest(TEST_TENANT_ID, TEST_PRODUCTS_IDS);

    private static final String TEST_PRODUCT_DELETE_JPQL =
            "delete from ProductEntity p where p.tenantId=1 and (p.id=4 or p.id=10 or p.id=15 )";

    @Test
    void shouldExecuteJpqlWrittenByRequest(){
        given(entityManager.createQuery(TEST_PRODUCT_DELETE_JPQL))
                .willReturn(productDeleteQuery);

        productDeletingService().execute(TEST_PRODUCT_DELETE_REQUEST);

        then(entityManager).should().createQuery(TEST_PRODUCT_DELETE_JPQL);
        then(productDeleteQuery).should().executeUpdate();
    }

    @Test
    void shouldReturnMapOfAffectedRowsAndQty(){
        given(entityManager.createQuery(TEST_PRODUCT_DELETE_JPQL))
                .willReturn(productDeleteQuery);
        given(productDeleteQuery.executeUpdate())
                .willReturn(1);

        var resultMap = productDeletingService().execute(TEST_PRODUCT_DELETE_REQUEST);

        assertThat(resultMap).containsExactlyEntriesOf(
                Map.ofEntries(entry("affected_rows", 1))
        );
    }

    private ProductDeletingService productDeletingService(){
        return new ProductDeletingService(entityManager);
    }
}
