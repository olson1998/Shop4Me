package com.shop4me.productdatastream.domain.service.persisting.product;

import com.shop4me.productdatastream.domain.model.request.product.ProductRequestGenerator;
import com.shop4me.productdatastream.domain.model.request.product.utils.ProductSearchFilter;
import com.shop4me.productdatastream.domain.port.requesting.ProductSearchRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static com.shop4me.productdatastream.domain.model.dao.productdatastorage.properties.ProductProperty.CATEGORY;
import static com.shop4me.productdatastream.domain.model.dao.productdatastorage.properties.ProductProperty.NAME;
import static com.shop4me.productdatastream.domain.model.request.enumset.Operator.LIKE;
import static com.shop4me.productdatastream.domain.model.request.enumset.Operator.NOT;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductSearchingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Long> productIdSearchQuery;

    private static final int TEST_TENANT_ID = 1;

    private static final ProductSearchFilter[] TEST_PRODUCT_SEARCH_FILTERS = {
            new ProductSearchFilter(NAME, NOT, "production"),
            new ProductSearchFilter(CATEGORY, LIKE, "\"all\"\"test\"")
    };

    private static final ProductSearchRequest TEST_PRODUCT_SEARCH_REQUEST =
            ProductRequestGenerator.productSearchRequest(TEST_TENANT_ID, TEST_PRODUCT_SEARCH_FILTERS);

    private static final String TEST_PRODUCT_SEARCH_JPQL =
            "select distinct p.id from ProductEntity p " +
                    "left outer join p.categoriesSet c where " +
                    "(p.name not like '%production%' ) and " +
                    "(concat(c.path, '.', '\"', c.name, '\"') like '\"all\"\"test\"%' ) " +
                    "and p.tenantId=1";

    @Test
    void shouldExecuteJpqlWrittenByRequest(){
        given(entityManager.createQuery(TEST_PRODUCT_SEARCH_JPQL, Long.class))
                .willReturn(productIdSearchQuery);

        productSearchingService().execute(TEST_PRODUCT_SEARCH_REQUEST);

        then(entityManager).should().createQuery(TEST_PRODUCT_SEARCH_JPQL, Long.class);
        then(productIdSearchQuery).should().getResultList();
    }

    private ProductSearchingService productSearchingService(){
        return new ProductSearchingService(entityManager);
    }
}
