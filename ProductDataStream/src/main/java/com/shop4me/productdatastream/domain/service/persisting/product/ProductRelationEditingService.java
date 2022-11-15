package com.shop4me.productdatastream.domain.service.persisting.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.model.request.utils.EntityRelationChange;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.shop4me.productdatastream.domain.model.dao.productdatastorage.properties.ProductProperty.CATEGORY;
import static com.shop4me.productdatastream.domain.model.dao.productdatastorage.properties.ProductProperty.ID;
import static com.shop4me.productdatastream.domain.model.request.utils.EntityRelationOperation.ADD;
import static com.shop4me.productdatastream.domain.model.request.utils.EntityRelationOperation.REMOVE;
import static com.shop4me.productdatastream.domain.service.persisting.product.ProductEditingService.CATEGORY_ADD_KEY;
import static com.shop4me.productdatastream.domain.service.persisting.product.ProductEditingService.CATEGORY_REMOVE_KEY;

@Slf4j

@RequiredArgsConstructor
public class ProductRelationEditingService {

    private final ObjectMapper objectMapper;

    public LinkedMultiValueMap<String, Long> editCategories(Map<String, String> productEditMap, TypedQuery<ProductEntity> selectTargetProductQuery){
        var resultMap = new LinkedMultiValueMap<String, Long>();
        var productId = productEditMap.get(ID.name());
        var json = productEditMap.get(CATEGORY.name());
        var relations = readEntityRelationChanges(json);
        log.info("CHANGING PRODUCT ID='{}' RELATIONS WITH CATEGORIES: {}", productId, relations);
        var product = selectTargetProductQuery.getSingleResult();

        Arrays.stream(relations).forEach(relation->{
            var categoriesIdJson = relation.getValue();
            var categoriesIds = readCategoriesIds(categoriesIdJson);

            if(relation.getEntityRelationOperation().equals(ADD)){
                addCollectedCategoriesIds(resultMap,CATEGORY_ADD_KEY, categoriesIds);

                var categories = createCategoryEntityList(categoriesIds);
                product.getCategoriesSet().addAll(categories);
            } else if (relation.getEntityRelationOperation().equals(REMOVE)) {
                addCollectedCategoriesIds(resultMap, CATEGORY_REMOVE_KEY, categoriesIds);

                product.getCategoriesSet()
                        .removeIf(category-> Arrays.stream(categoriesIds)
                                .anyMatch(categoryId-> categoryId == category.getId()));
            }
        });
        return resultMap;
    }

    private List<CategoryEntity> createCategoryEntityList(long[] ids){
        return Arrays.stream(ids)
                .mapToObj(CategoryEntity::new)
                .toList();
    }

    @SneakyThrows
    private EntityRelationChange[] readEntityRelationChanges(String json){
        return objectMapper.readValue(json, EntityRelationChange[].class);
    }

    @SneakyThrows
    private long[] readCategoriesIds(String json){
        return objectMapper.readValue(json, long[].class);
    }

    private void addCollectedCategoriesIds(MultiValueMap<String, Long> resultMap, String key, long[] collectedIds){
        for(long id : collectedIds){
            resultMap.add(key, id);
        }
    }
}
