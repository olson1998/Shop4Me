package com.shop4me.productdatastream.domain.service.persisting.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.model.data.dto.Category;
import com.shop4me.productdatastream.domain.model.data.dto.ImageUrl;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ImageUrlEntity;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.port.persisting.repositories.product.ProductEditingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ProductEditRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.product.ProductProperty.*;
import static java.util.Map.entry;

@Slf4j

@AllArgsConstructor
public class ProductEditingService implements ProductEditingExecutor {

    private final EntityManager productEntityManager;

    private final ObjectMapper mapper;

    @Override
    public Map<String, Integer> execute(@NotNull ProductEditRequest request){
        int affectedRows = 0;

        var editMap = request.getEditMapCopy();

        var id = Long.parseLong(editMap.get(ID.name()));

        log.info(request.toString());

        var containsKeyPhotoUrlOrCategory = editMap.containsKey(PHOTO_URL.name()) || editMap.containsKey(CATEGORY.name());

        if(containsKeyPhotoUrlOrCategory){
            var product = getProductByProductId(productEntityManager, id);

            if(editMap.containsKey(CATEGORY.name())){
                affectedRows = editProductCategories(editMap, product);
            }
            if(editMap.containsKey(PHOTO_URL.name())){
                affectedRows = editProductImageUrls(editMap, product);
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

    private ProductEntity getProductByProductId(EntityManager entityManager, long id){
        return entityManager.createQuery("select p from ProductEntity p where p.id = :id", ProductEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    private int editProductCategories(@NonNull Map<String, String> editMap, @NonNull ProductEntity product){
        try{
            var categoriesBase64 = editMap.get(CATEGORY.name());
            var categoriesJson = new String(Base64.getDecoder().decode(categoriesBase64.getBytes(StandardCharsets.UTF_8)));
            var categories = mapper.readValue(categoriesJson, Category[].class);
            var toSaveCategoriesSet = writeCategoryEntitySet(categories);

            product.setCategoriesSet(toSaveCategoriesSet);
            return 1;
        }catch (JsonProcessingException e){
            log.warn("Could not read Categories..., Reason: {}", e.getMessage());
            return 0;
        }
    }

    private int editProductImageUrls(@NonNull Map<String, String> editMap,@NonNull ProductEntity product){
        try{
            var imagesBase64 = editMap.get(PHOTO_URL.name());
            var imagesJson = new String(Base64.getDecoder().decode(imagesBase64.getBytes(StandardCharsets.UTF_8)));
            var images = mapper.readValue(imagesJson, ImageUrl[].class);
            var imageUrlSet = writeImageUrlEntitySet(images);

            product.setImageUrlSet(imageUrlSet);
            return  1;
        }catch (JsonProcessingException e){
            log.warn("Could not read Image url..., Reason: {}", e.getMessage());
            return 0;
        }
    }

    private Set<CategoryEntity> writeCategoryEntitySet(Category[] categories){
        return Arrays.stream(categories)
                .map(category -> new CategoryEntity(
                        category.getId(),
                        category.getName(),
                        category.getPath()
                )).collect(Collectors.toSet());
    }

    private Set<ImageUrlEntity> writeImageUrlEntitySet(ImageUrl[] imageUrls){
        return Arrays.stream(imageUrls)
                .map(imageUrl -> new ImageUrlEntity(
                        imageUrl.getId(),
                        imageUrl.getProductId(),
                        imageUrl.getUrl(),
                        imageUrl.getVisibility()
                )).collect(Collectors.toSet());
    }
}
