package com.shop4me.productdatastream.domain.service.persiting.product;

import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.service.persisting.product.ProductObtainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static com.shop4me.productdatastream.domain.model.request.ProductOperationRequestTestImpl.productObtainRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductObtainingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<ProductEntity> query;

    private final long[] idArray = {1L, 2L, 3L};

    private static final String PRODUCT_OBTAIN_JPQL =
            "select p from ProductEntity p where p.id=1 or p.id=2 or p.id=3 ";

    @Test
    void shouldExecuteJpqlCreatedByRequest(){
        given(entityManager.createQuery(PRODUCT_OBTAIN_JPQL, ProductEntity.class))
                .willReturn(query);

        productObtainingService().execute(productObtainRequest(idArray));

        then(entityManager).should().createQuery(PRODUCT_OBTAIN_JPQL, ProductEntity.class);
    }

    @Test
    void shouldQuerySelectionResult(){
        given(entityManager.createQuery(PRODUCT_OBTAIN_JPQL, ProductEntity.class))
                .willReturn(query);

        productObtainingService().execute(productObtainRequest(idArray));

        then(query).should().getResultList();
    }

    private ProductObtainingService productObtainingService(){
        return new ProductObtainingService(entityManager);
    }
}
