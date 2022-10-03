package com.shop4me.productdatastream.domain.service.persiting.product;

import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.product.ProductProperty;
import com.shop4me.productdatastream.domain.model.request.enumset.Operator;
import com.shop4me.productdatastream.domain.model.request.product.tools.ProductSearchFilter;
import com.shop4me.productdatastream.domain.service.persisting.product.ProductSearchingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.product.ProductProperty.*;
import static com.shop4me.productdatastream.domain.model.request.ProductOperationRequestTestImpl.productSearchRequest;
import static com.shop4me.productdatastream.domain.model.request.enumset.Operator.LIKE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductSearchingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Long> productIdSearchQuery;

    @Mock
    private List<Long> selectedProductIdList;

    private static final String SEARCHED_PRODUCT_NAME = "test";

    private static final String SEARCHED_PRODUCT_ABSOLUTE_PATH = "\"all\".\"test\"";

    private static final ProductSearchFilter[] PRODUCT_SEARCH_FILTERS  = {
            new ProductSearchFilter(NAME, LIKE, SEARCHED_PRODUCT_NAME)
    };

    private static final ProductSearchFilter[] PRODUCT_SEARCH_FILTERS_WITH_JOIN_ENTITY = {
            new ProductSearchFilter(NAME, LIKE, SEARCHED_PRODUCT_NAME),
            new ProductSearchFilter(CATEGORY, LIKE, SEARCHED_PRODUCT_ABSOLUTE_PATH)
    };

    private static final String SEARCHED_PRODUCT_JPQL =
            "select distinct p.id from ProductEntity p where (p.name like '%"+ SEARCHED_PRODUCT_NAME + "%' ) ";

    private static final String SEARCHED_PRODUCT_JPQL_WITH_JOINED_ENTITY =
            "select distinct p.id from ProductEntity p left outer join p.categoriesSet c where (p.name like '%" + SEARCHED_PRODUCT_NAME + "%' ) " +
            "and (concat(c.path, '.', '\"', c.name, '\"') like '" + SEARCHED_PRODUCT_ABSOLUTE_PATH + "%' ) ";

    @Test
    void shouldExecuteJpqlWrittenByRequest(){
        given(entityManager.createQuery(SEARCHED_PRODUCT_JPQL, Long.class))
                .willReturn(productIdSearchQuery);

        productSearchingService().execute(productSearchRequest(PRODUCT_SEARCH_FILTERS));

        then(productIdSearchQuery).should().getResultList();
    }

    @Test
    void shouldExecuteJpqlWrittenByRequestWithJoinedEntity(){
        given(entityManager.createQuery(SEARCHED_PRODUCT_JPQL_WITH_JOINED_ENTITY, Long.class))
                .willReturn(productIdSearchQuery);

        productSearchingService().execute(productSearchRequest(PRODUCT_SEARCH_FILTERS_WITH_JOIN_ENTITY));

        then(productIdSearchQuery).should().getResultList();
    }

    @Test
    void shouldReturnListOfObtainedProductIds(){
        given(entityManager.createQuery(SEARCHED_PRODUCT_JPQL_WITH_JOINED_ENTITY, Long.class))
                .willReturn(productIdSearchQuery);
        given(productIdSearchQuery.getResultList())
                .willReturn(selectedProductIdList);

        var resultList =
                productSearchingService().execute(productSearchRequest(PRODUCT_SEARCH_FILTERS_WITH_JOIN_ENTITY));

        then(productIdSearchQuery).should().getResultList();

        assertThat(resultList).isEqualTo(selectedProductIdList);
    }

    private ProductSearchingService productSearchingService(){
        return new ProductSearchingService(entityManager);
    }
}
