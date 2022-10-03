package com.shop4me.productdatastream.domain.service.persisting.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.port.persisting.repositories.product.ProductEditingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ProductEditRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.Map;

import static com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.product.ProductProperty.*;
import static java.util.Map.entry;

@Slf4j
public class ProductEditingService implements ProductEditingExecutor {

    private final EntityManager productEntityManager;

    private final ProductEditingCategoriesService productEditingCategoriesService;

    private final ProductEditingImageUrlService productEditingImageUrlService;

    @Override
    public Map<String, Integer> execute(@NotNull ProductEditRequest request){
        int affectedRows = 0;
        var editMap = request.getEditMapCopy();
        var id = Long.parseLong(editMap.get(ID.name()));

        log.info(request.toString());

        var containsKeyPhotoUrlOrCategory = editMap.containsKey(PHOTO_URL.name()) || editMap.containsKey(CATEGORY.name());

        if(containsKeyPhotoUrlOrCategory){
            var product = getProductById(id);
            if(editMap.containsKey(CATEGORY.name())){
                var value = editMap.get(CATEGORY.name());
                affectedRows = productEditingCategoriesService.edit(product, value);
            }
            if(editMap.containsKey(PHOTO_URL.name())){
                var value = editMap.get(PHOTO_URL.name());
                affectedRows = productEditingImageUrlService.edit(product, value);
            }
        }
        if(editMap.containsKey(NAME.name()) || editMap.containsKey(DESCRIPTION.name())){
            var jpql = request.writeJpqlQuery();

            affectedRows = executeJpqlQuery(productEntityManager, jpql);
        }
        return Map.ofEntries(
                entry("affected_rows", affectedRows)
        );
    }

    private int executeJpqlQuery(EntityManager entityManager, String jpql){
        return entityManager.createQuery(jpql)
                .executeUpdate();
    }

    private ProductEntity getProductById(long id){
        return productEntityManager
                .createQuery("select p from ProductEntity p where p.id=: id", ProductEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public ProductEditingService(EntityManager productEntityManager, ObjectMapper mapper) {
        this.productEntityManager = productEntityManager;
        this.productEditingCategoriesService = new ProductEditingCategoriesService(mapper);
        this.productEditingImageUrlService = new ProductEditingImageUrlService(mapper);
    }
}
