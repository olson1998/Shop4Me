package com.shop4me.productdatastream.domain.service.persiting.product;

import com.shop4me.productdatastream.domain.model.data.dto.Product;
import com.shop4me.productdatastream.domain.service.persisting.product.ProductDeletingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static com.shop4me.productdatastream.domain.model.request.ProductOperationRequestTestImpl.productDeleteRequest;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductDeletingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query productDeletingQuery;

    private static final long TEST_PRODUCT_ID = 1L;

    private static final String TEST_PRODUCT_NAME = "test-product-1";

    private static final Product TEST_PRODUCT = new Product(TEST_PRODUCT_ID, TEST_PRODUCT_NAME, null, null, null, null, null);

    private static final String TEST_DELETING_PRODUCT_JPQL =
            "delete from ProductEntity p where p.id= " + TEST_PRODUCT_ID + " and p.name= '" + TEST_PRODUCT_NAME + "'";

    @Test
    void shouldExecuteUpdateWrittenByRequest(){
        given(entityManager.createQuery(TEST_DELETING_PRODUCT_JPQL))
                .willReturn(productDeletingQuery);

        productDeletingService().execute(productDeleteRequest(TEST_PRODUCT));

        then(productDeletingQuery).should().executeUpdate();
    }

    @Test
    void shouldReturnMapOfAffectedRowsAndItsQuantity(){
        given(entityManager.createQuery(TEST_DELETING_PRODUCT_JPQL))
                .willReturn(productDeletingQuery);
        given(productDeletingQuery.executeUpdate())
                .willReturn(1);

        var affectedRowsMap =
                productDeletingService().execute(productDeleteRequest(TEST_PRODUCT));

        assertThat(affectedRowsMap).containsOnly(entry("affected_rows", 1));
    }

    private ProductDeletingService productDeletingService(){
        return new ProductDeletingService(entityManager);
    }
}
