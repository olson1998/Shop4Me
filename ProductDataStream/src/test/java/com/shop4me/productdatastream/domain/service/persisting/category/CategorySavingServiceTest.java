package com.shop4me.productdatastream.domain.service.persisting.category;

import com.shop4me.productdatastream.domain.model.dao.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.model.dto.Category;
import com.shop4me.productdatastream.domain.model.request.category.CategoryRequestGenerator;
import com.shop4me.productdatastream.domain.port.objects.dto.CategoryDto;
import com.shop4me.productdatastream.domain.port.requesting.CategorySaveRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import java.util.Map;
import java.util.UUID;

import static com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus.FAILURE;
import static com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus.SUCCESS;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategorySavingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Boolean> uniqueCheckingQuery;

    @Captor
    private ArgumentCaptor<CategoryEntity> categoryEntityArgumentCaptor;

    private static final int TEST_TENANT_ID = 1;

    private static final String TEST_PATH = "\"all\".\"test\"";

    private static final String TEST_CATEGORY_1_ID = UUID.randomUUID().toString();

    private static final String TEST_CATEGORY_2_ID = UUID.randomUUID().toString();

    private static final String TEST_CATEGORY_3_ID = UUID.randomUUID().toString();

    private static final Category TEST_CATEGORY_1 = new Category(TEST_TENANT_ID, TEST_PATH, "test-1");

    private static final Category TEST_CATEGORY_2 = new Category(TEST_TENANT_ID, TEST_PATH, "test-2");

    private static final Category TEST_CATEGORY_3 = new Category(TEST_TENANT_ID, TEST_PATH, "test-3");

    private static final Map<String, Category> TEST_CATEGORY_SAVE_MAP = Map.ofEntries(
            entry(TEST_CATEGORY_1_ID, TEST_CATEGORY_1),
            entry(TEST_CATEGORY_2_ID, TEST_CATEGORY_2),
            entry(TEST_CATEGORY_3_ID, TEST_CATEGORY_3)
    );

    private static final CategorySaveRequest TEST_CATEGORY_SAVE_REQUEST =
            CategoryRequestGenerator.categorySaveRequest(TEST_TENANT_ID, TEST_CATEGORY_SAVE_MAP);

    private static final String CHECK_CATEGORY_UNIQUE =
            "select case when count(c.name)=0 " +
            "then true else false end from CategoryEntity c " +
            "where concat(c.path, '.', '\"', c.name, '\"')= :absolutePath " +
            "and c.tenantId=:tenant";

    @Test
    void shouldCheckIfGivenCategoriesAlreadyExist(){
        var qty = TEST_CATEGORY_SAVE_REQUEST.getCategoriesToSaveMap().size();

        given(entityManager.createQuery(CHECK_CATEGORY_UNIQUE, Boolean.class))
                .willReturn(uniqueCheckingQuery);
        given(uniqueCheckingQuery.setParameter("tenant", TEST_TENANT_ID))
                .willReturn(uniqueCheckingQuery);
        TEST_CATEGORY_SAVE_REQUEST.getCategoriesToSaveMap().values().forEach(category->{
            given(uniqueCheckingQuery.setParameter("absolutePath", category.getAbsolutePath()))
                    .willReturn(uniqueCheckingQuery);
        });
        given(uniqueCheckingQuery.getSingleResult()).willReturn(true);

        categorySaveService().execute(TEST_CATEGORY_SAVE_REQUEST);

        then(entityManager).should(times(qty)).createQuery(CHECK_CATEGORY_UNIQUE, Boolean.class);
        then(uniqueCheckingQuery).should(times(qty)).setParameter("tenant", TEST_TENANT_ID);
        TEST_CATEGORY_SAVE_REQUEST.getCategoriesToSaveMap().values().forEach(category->{
            then(uniqueCheckingQuery).should().setParameter("absolutePath", category.getAbsolutePath());
        });
    }

    @Test
    void shouldPersistAllOfTheEntitiesIfItsUnique(){
        given(entityManager.createQuery(CHECK_CATEGORY_UNIQUE, Boolean.class))
                .willReturn(uniqueCheckingQuery);
        given(uniqueCheckingQuery.setParameter("tenant", TEST_TENANT_ID))
                .willReturn(uniqueCheckingQuery);
        TEST_CATEGORY_SAVE_REQUEST.getCategoriesToSaveMap().values().forEach(category->{
            given(uniqueCheckingQuery.setParameter("absolutePath", category.getAbsolutePath()))
                    .willReturn(uniqueCheckingQuery);
        });
        given(uniqueCheckingQuery.getSingleResult()).willReturn(true);

        categorySaveService().execute(TEST_CATEGORY_SAVE_REQUEST);

        then(entityManager).should(atLeastOnce()).persist(categoryEntityArgumentCaptor.capture());

        var categories= categoryEntityArgumentCaptor.getAllValues();
        var absolutePaths = TEST_CATEGORY_SAVE_REQUEST.getCategoriesToSaveMap()
                .values()
                .stream()
                .map(CategoryDto::getAbsolutePath)
                .toList();
        var persistedAbsolutePaths = categories.stream()
                .map(category-> category.getPath()+".\""+category.getName() + "\"")
                .toList();

        assertThat(categories).allMatch(category-> category.getTenantId() == TEST_TENANT_ID);
        assertThat(persistedAbsolutePaths).containsAll(absolutePaths);
    }

    @Test
    void shouldReturnMapOfCorrelationIdAndSuccessIfPersistedGivenCategory(){
        given(entityManager.createQuery(CHECK_CATEGORY_UNIQUE, Boolean.class))
                .willReturn(uniqueCheckingQuery);
        given(uniqueCheckingQuery.setParameter("tenant", TEST_TENANT_ID))
                .willReturn(uniqueCheckingQuery);
        TEST_CATEGORY_SAVE_REQUEST.getCategoriesToSaveMap().values().forEach(category->{
            given(uniqueCheckingQuery.setParameter("absolutePath", category.getAbsolutePath()))
                    .willReturn(uniqueCheckingQuery);
        });
        given(uniqueCheckingQuery.getSingleResult()).willReturn(true);

        var resultMap = categorySaveService().execute(TEST_CATEGORY_SAVE_REQUEST);

        assertThat(resultMap.values()).allMatch(result-> result.equals(SUCCESS.name()));
    }

    @Test
    void shouldReturnMapOfCorrelationIdAndFailureIfCategoryAlreadyExists(){
        given(entityManager.createQuery(CHECK_CATEGORY_UNIQUE, Boolean.class))
                .willReturn(uniqueCheckingQuery);
        given(uniqueCheckingQuery.setParameter("tenant", TEST_TENANT_ID))
                .willReturn(uniqueCheckingQuery);
        TEST_CATEGORY_SAVE_REQUEST.getCategoriesToSaveMap().values().forEach(category->{
            given(uniqueCheckingQuery.setParameter("absolutePath", category.getAbsolutePath()))
                    .willReturn(uniqueCheckingQuery);
        });
        given(uniqueCheckingQuery.getSingleResult()).willReturn(false);

        var resultMap = categorySaveService().execute(TEST_CATEGORY_SAVE_REQUEST);

        assertThat(resultMap.values()).allMatch(result-> result.equals(FAILURE.name()));
    }

    @Test
    void shouldReturnMapOfCorrelationIdAndFailureIfEntityManagerThrowsException(){
        given(entityManager.createQuery(CHECK_CATEGORY_UNIQUE, Boolean.class))
                .willReturn(uniqueCheckingQuery);
        given(uniqueCheckingQuery.setParameter("tenant", TEST_TENANT_ID))
                .willReturn(uniqueCheckingQuery);
        TEST_CATEGORY_SAVE_REQUEST.getCategoriesToSaveMap().values().forEach(category->{
            given(uniqueCheckingQuery.setParameter("absolutePath", category.getAbsolutePath()))
                    .willReturn(uniqueCheckingQuery);
        });
        given(uniqueCheckingQuery.getSingleResult()).willReturn(true);
        doThrow(TransactionRequiredException.class).when(entityManager).persist(any(CategoryEntity.class));

        var resultMap = categorySaveService().execute(TEST_CATEGORY_SAVE_REQUEST);

        assertThat(resultMap.values()).allMatch(result-> result.equals(FAILURE.name()));
    }

    private CategorySaveService categorySaveService(){
        return new CategorySaveService(entityManager);
    }
}
