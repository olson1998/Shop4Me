package com.shop4me.productdatastream.domain.service.persisting.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.properties.ProductProperty;
import com.shop4me.productdatastream.domain.model.request.product.ProductRequestGenerator;
import com.shop4me.productdatastream.domain.port.requesting.ProductEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.shop4me.productdatastream.domain.model.dao.productdatastorage.properties.ProductProperty.*;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductEditingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private ProductEntity updatedProduct;

    @Mock
    private Set<CategoryEntity> updatedProductCategoriesSet;

    @Mock
    private Query productUpdateQuery;

    @Mock
    private TypedQuery<ProductEntity> selectUpdatedProductQuery;

    @Captor
    private ArgumentCaptor<Collection<? extends CategoryEntity>> categoriesCaptor;

    private static final int TEST_TENANT_ID = 1;

    private static final long TEST_PRODUCT_ID = 1L;

    private static final List<Long> UPDATED_PRODUCT_CATEGORIES= List.of(1L,5L,6L);

    private static final String UPDATED_PRODUCT_CATEGORIES_STRING = "[{\"operation\":\"ADD\",\"value\":\"[1,5,6]\"}]";

    private static final String TEST_PRODUCT_ID_STRING = TEST_PRODUCT_ID+"";

    private static final Map<ProductProperty, String> TEST_NOT_RELATED_PRODUCT_UPDATE_MAP = Map.ofEntries(
            entry(ID, TEST_PRODUCT_ID_STRING),
            entry(NAME,"overwritten name"),
            entry(DESCRIPTION, "overwritten description")
    );

    private static final Map<ProductProperty, String> TEST_RELATED_PRODUCT_UPDATE_MAP = Map.ofEntries(
            entry(ID, TEST_PRODUCT_ID_STRING),
            entry(CATEGORY, UPDATED_PRODUCT_CATEGORIES_STRING)
    );

    private static final ProductEditRequest TEST_PRODUCT_EDIT_REQUEST_NOT_RELATED_UPDATED =
            ProductRequestGenerator.productEditRequest(TEST_TENANT_ID, TEST_NOT_RELATED_PRODUCT_UPDATE_MAP);

    private static final ProductEditRequest TEST_PRODUCT_EDIT_REQUEST_RELATION_UPDATED =
            ProductRequestGenerator.productEditRequest(TEST_TENANT_ID, TEST_RELATED_PRODUCT_UPDATE_MAP);

    private static final String TEST_NOT_RELATED_PRODUCT_UPDATE_JPQL =
            "update ProductEntity p set p.name='overwritten name', p.description='overwritten description' " +
                    "where p.id=1 and p.tenantId=1";

    private static final String SELECT_EDITED_PRODUCT_JPQL =
            "select p from ProductEntity p where p.tenantId=:tenant and p.id= :id";

    @Test
    void shouldExecuteUpdateJpqlIfOnlyProductPropertiesUpdated(){
        given(entityManager.createQuery(TEST_NOT_RELATED_PRODUCT_UPDATE_JPQL))
                .willReturn(productUpdateQuery);
        given(entityManager.createQuery(SELECT_EDITED_PRODUCT_JPQL, ProductEntity.class))
                .willReturn(selectUpdatedProductQuery);
        given(selectUpdatedProductQuery.setParameter("tenant", TEST_TENANT_ID))
                .willReturn(selectUpdatedProductQuery);
        given(selectUpdatedProductQuery.setParameter("id", TEST_PRODUCT_ID))
                .willReturn(selectUpdatedProductQuery);
        given(selectUpdatedProductQuery.getSingleResult())
                .willReturn(updatedProduct);
        given(updatedProduct.getName())
                .willReturn("overwritten name");
        given(updatedProduct.getDescription())
                .willReturn("overwritten description");

        productEditingService().execute(TEST_PRODUCT_EDIT_REQUEST_NOT_RELATED_UPDATED);

        then(entityManager).should().createQuery(TEST_NOT_RELATED_PRODUCT_UPDATE_JPQL);
        then(productUpdateQuery).should().executeUpdate();
    }

    @Test
    void shouldSelectUpdatedProductEntityIfRelationIsUpdated(){
        given(entityManager.createQuery(SELECT_EDITED_PRODUCT_JPQL, ProductEntity.class))
                .willReturn(selectUpdatedProductQuery);
        given(selectUpdatedProductQuery.setParameter("id", TEST_PRODUCT_ID))
                .willReturn(selectUpdatedProductQuery);
        given(selectUpdatedProductQuery.setParameter("tenant", TEST_TENANT_ID))
                .willReturn(selectUpdatedProductQuery);
        given(selectUpdatedProductQuery.getSingleResult())
                .willReturn(updatedProduct);
        given(updatedProduct.getCategoriesSet())
                .willReturn(updatedProductCategoriesSet);

        productEditingService().execute(TEST_PRODUCT_EDIT_REQUEST_RELATION_UPDATED);

        then(updatedProductCategoriesSet).should().addAll(categoriesCaptor.capture());

        var categoriesIdList = categoriesCaptor.getValue().stream()
                .map(CategoryEntity::getId)
                .toList();

        assertThat(categoriesIdList).containsExactlyInAnyOrderElementsOf(UPDATED_PRODUCT_CATEGORIES);
    }

    private ProductEditingService productEditingService(){
        return new ProductEditingService(entityManager, objectMapper);
    }
}
