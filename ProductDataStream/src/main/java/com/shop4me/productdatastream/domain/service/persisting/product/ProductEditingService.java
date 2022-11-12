package com.shop4me.productdatastream.domain.service.persisting.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.port.persisting.product.ProductEditingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ProductEditRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.shop4me.productdatastream.domain.model.dao.productdatastorage.properties.ProductProperty.*;
import static com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus.FAILURE;
import static com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus.SUCCESS;

@Slf4j
public class ProductEditingService implements ProductEditingExecutor {

    private final EntityManager entityManager;

    private final ProductRelationEditingService productRelationEditingService;

    protected static final String CATEGORY_ADD_KEY= "CATEGORY ADD";

    protected static final String CATEGORY_REMOVE_KEY = "CATEGORY REMOVE";

    protected static final String IMAGE_URL_ADD_KEY= "IMAGE_URL ADD";

    protected static final String IMAGE_URL_REMOVE = "IMAGE_URL REMOVE";
    @Override
    public Map<String, String> execute(@NotNull ProductEditRequest request){
        LinkedMultiValueMap<String, Long> categoriesEditResultMap = null;

        var editMap = request.getEditMapCopy();
        var id = Long.parseLong(editMap.get(ID.name()));
        var query = getTargetProductQuery(request.getTenantId(), id);

        log.info(request.toString());

        var containsKeyCategory = editMap.containsKey(CATEGORY.name());
        var containsKeyNameOrDescription = editMap.containsKey(NAME.name()) || editMap.containsKey(DESCRIPTION.name());
        if(containsKeyNameOrDescription){
            var jpql = request.writeJpqlQuery();
            entityManager.createQuery(jpql)
                    .executeUpdate();
        }
        if(containsKeyCategory){
            categoriesEditResultMap = productRelationEditingService.editCategories(editMap, query);
        }
        return prepareResultMap(editMap, categoriesEditResultMap, query);
    }

    private Map<String, String> prepareResultMap(Map<String, String> editMap,
                                                 LinkedMultiValueMap<String, Long> categoriesEditResultMap,
                                                 TypedQuery<ProductEntity> query){
        var resultMap = new HashMap<String, String>();
        var product = query.getSingleResult();
        if(editMap.containsKey(NAME.name())){
            var value = editMap.get(NAME.name());
            if(product.getName().equals(value)){
                resultMap.put(NAME.name(), SUCCESS.name());
            }else {
                resultMap.put(NAME.name(), SUCCESS.name());
            }
        }
        if(editMap.containsKey(DESCRIPTION.name())){
            var value = editMap.get(DESCRIPTION.name());
            if(product.getDescription().equals(value)){
                resultMap.put(DESCRIPTION.name(), SUCCESS.name());
            }else {
                resultMap.put(DESCRIPTION.name(), SUCCESS.name());
            }
        }
        if(categoriesEditResultMap != null){
            var productCategoriesIds = product.getCategoriesSet().stream()
                    .map(CategoryEntity::getId)
                    .toList();

            if(categoriesEditResultMap.containsKey(CATEGORY_ADD_KEY)){
                var added = categoriesEditResultMap.get(CATEGORY_ADD_KEY);
                if(added!=null){
                    if(new HashSet<>(productCategoriesIds).containsAll(added)){
                        resultMap.put(CATEGORY_ADD_KEY, SUCCESS.name());
                    }else {
                        resultMap.put(CATEGORY_ADD_KEY, FAILURE.name());
                    }
                }
            }
            if(categoriesEditResultMap.containsKey(CATEGORY_REMOVE_KEY)){
                var removed = categoriesEditResultMap.get(CATEGORY_REMOVE_KEY);
                if(removed !=null){
                    if(!new HashSet<>(productCategoriesIds).containsAll(removed)){
                        resultMap.put(CATEGORY_REMOVE_KEY, SUCCESS.name());
                    }else {
                        resultMap.put(CATEGORY_REMOVE_KEY, FAILURE.name());
                    }
                }
            }
        }
        return resultMap;
    }
    private TypedQuery<ProductEntity> getTargetProductQuery(int tenantId, long id){
        return entityManager
                .createQuery("select p from ProductEntity p where p.tenantId=:tenant and p.id= :id", ProductEntity.class)
                .setParameter("tenant", tenantId)
                .setParameter("id", id);
    }
    public ProductEditingService(EntityManager productEntityManager, ObjectMapper mapper) {
        this.entityManager = productEntityManager;
        this.productRelationEditingService = new ProductRelationEditingService(mapper);
    }
}
