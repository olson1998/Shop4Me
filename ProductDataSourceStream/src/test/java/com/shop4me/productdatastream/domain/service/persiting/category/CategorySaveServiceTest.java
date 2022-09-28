package com.shop4me.productdatastream.domain.service.persiting.category;

import com.shop4me.productdatastream.domain.model.data.dto.Category;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;
import com.shop4me.productdatastream.domain.service.persisting.category.CategorySaveService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.shop4me.productdatastream.domain.model.request.CategoryOperationRequestTestImpl.categorySaveRequest;
import static com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus.FAILURE;
import static com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CategorySaveServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Boolean> checkIfCategoryUniqueQuery;

    private final Map<String, CategoryDto> testPayload = createCategoryObtainRequestPayload(
            new Category(null, "\"all\"", "cat"),
            new Category(null, "\"all\"", "dog"),
            new Category(null, "\"all\"", "parrot")
    );

    private final String checkIfAbsolutePathExistsJpql = "select case when count(c.name)=0 " +
            "then true else false end from CategoryEntity c " +
            "where concat(c.path, '.', '\"', c.name, '\"')= :absolutePath";

    @Test
    void shouldPersistAllCategoriesFromMap(){
        var request = categorySaveRequest(testPayload);

        given(entityManager.createQuery(checkIfAbsolutePathExistsJpql, Boolean.class))
                .willReturn(checkIfCategoryUniqueQuery);
        testPayload.values().forEach(category -> {
            given(checkIfCategoryUniqueQuery.setParameter("absolutePath", category.getAbsolutePath()))
                    .willReturn(checkIfCategoryUniqueQuery);
        });
        given(checkIfCategoryUniqueQuery.getSingleResult())
                .willReturn(true);

        categorySaveService().execute(request);

        then(entityManager).should(times(testPayload.size()))
                .persist(any(CategoryEntity.class));
    }

    @Test
    void shouldNotPersistCategoryIfAbsolutePathIsNotUnique(){
        var request =  categorySaveRequest(testPayload);

        given(entityManager.createQuery(checkIfAbsolutePathExistsJpql, Boolean.class))
                .willReturn(checkIfCategoryUniqueQuery);
        testPayload.values().forEach(category -> {
            given(checkIfCategoryUniqueQuery.setParameter("absolutePath", category.getAbsolutePath()))
                    .willReturn(checkIfCategoryUniqueQuery);
        });
        given(checkIfCategoryUniqueQuery.getSingleResult())
                .willReturn(false);

        categorySaveService().execute(request);

        then(entityManager).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldReturnMapOfCorrelationIdAndExecuteStatusSuccessIfSuccsededToPersistTheEntity(){
        var category = new Category(null, "\"all\"", "test");
        var payload = createCategoryObtainRequestPayload(category);
        var request =  categorySaveRequest(payload);

        given(entityManager.createQuery(checkIfAbsolutePathExistsJpql, Boolean.class))
                .willReturn(checkIfCategoryUniqueQuery);
        payload.values().forEach(categoryDto -> {
            given(checkIfCategoryUniqueQuery.setParameter("absolutePath", categoryDto.getAbsolutePath()))
                    .willReturn(checkIfCategoryUniqueQuery);
        });
        given(checkIfCategoryUniqueQuery.getSingleResult())
                .willReturn(true);

        var resultMap = categorySaveService().execute(request);

        resultMap.values().forEach(resultStat -> assertThat(resultStat).isEqualTo(SUCCESS.name()));
    }

    @Test
    void shouldReturnMapOfCorrelationIdAndExecuteStatusFailureIfFailedToPersistTheEntity(){
        var category = new Category(null, "\"all\"", "test");
        var payload = createCategoryObtainRequestPayload(category);
        var request =  categorySaveRequest(payload);

        given(entityManager.createQuery(checkIfAbsolutePathExistsJpql, Boolean.class))
                .willReturn(checkIfCategoryUniqueQuery);
        payload.values().forEach(categoryDto -> {
            given(checkIfCategoryUniqueQuery.setParameter("absolutePath", categoryDto.getAbsolutePath()))
                    .willReturn(checkIfCategoryUniqueQuery);
        });
        given(checkIfCategoryUniqueQuery.getSingleResult())
                .willReturn(false);

        var resultMap = categorySaveService().execute(request);

        resultMap.values().forEach(resultStat -> assertThat(resultStat).isEqualTo(FAILURE.name()));
    }

    private CategorySaveService categorySaveService(){
        return new CategorySaveService(entityManager);
    }

    private Map<String, CategoryDto> createCategoryObtainRequestPayload(CategoryDto ... categories){
        var payload = new HashMap<String, CategoryDto>();
        Arrays.stream(categories).forEach(category -> {
            var correlationId = UUID.randomUUID().toString();

            payload.put(correlationId, category);
        });
        return payload;
    }
}
