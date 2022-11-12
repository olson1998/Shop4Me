package com.shop4me.productdatastream.domain.service.persisting.product;

import com.shop4me.productdatastream.domain.model.dao.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.model.request.product.ProductRequestGenerator;
import com.shop4me.productdatastream.domain.port.requesting.ProductObtainRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductObtainingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<ProductEntity> productObtainQuery;

    private static final int TEST_TENANT_ID = 1;

    private static final long[] TEST_PRODUCT_OBTAIN_ID_ARRAY = {1L, 4L, 5L};

    private static final ProductObtainRequest PRODUCT_OBTAIN_REQUEST =
            ProductRequestGenerator.productObtainRequest(TEST_TENANT_ID, TEST_PRODUCT_OBTAIN_ID_ARRAY );

    private static final String TEST_PRODUCT_OBTAIN_JPQL =
            "select p from ProductEntity p where (p.id=1 or p.id=4 or p.id=5 ) and p.tenantId=1";

    @Test
    void shouldGetResultListFromExecutionOfJpqlWrittenByRequest(){
        given(entityManager.createQuery(TEST_PRODUCT_OBTAIN_JPQL, ProductEntity.class))
                .willReturn(productObtainQuery);
        given(productObtainQuery.getResultList())
                .willReturn(new ArrayList<>());

        productObtainingService().execute(PRODUCT_OBTAIN_REQUEST);

        then(entityManager).should().createQuery(TEST_PRODUCT_OBTAIN_JPQL, ProductEntity.class);
        then(productObtainQuery).should().getResultList();
    }

    private ProductObtainingService productObtainingService(){
        return new ProductObtainingService(entityManager);
    }
}
