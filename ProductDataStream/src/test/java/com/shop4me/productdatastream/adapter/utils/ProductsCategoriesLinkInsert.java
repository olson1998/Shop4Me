package com.shop4me.productdatastream.adapter.utils;

import com.shop4me.productdatastream.domain.model.data.dto.Category;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ProductsCategoriesLinkInsert extends ExampleEntitiesInsertInterface {

    LinkedMultiValueMap<String, String> productsImplementingCategories();

    default void createProductImplementingCategoriesWeb(){
        productsImplementingCategories().forEach((productName, appendedAbsolutePaths) -> {
            log().info("linking: '{}' with: {}", productName, appendedAbsolutePaths);

            var product = selectProductByName(productName);
            var appendedCategories = selectCategoriesByNames(appendedAbsolutePaths);

            product.setCategoriesSet(appendedCategories);
        });
    }

    default void linkProductWithCategories(
            String productName,
            List<String> absolutePaths){
        productsImplementingCategories().put(productName, absolutePaths);
    }

    default List<String> absolutePathList(Category ... categories){
        return Arrays.stream(categories)
                .map(Category::getAbsolutePath)
                .toList();
    }

    default void cleanAllLinks(){
        var size = productsImplementingCategories().values().size();
        log().info("Cleaning all {} links", size);
        productsImplementingCategories().clear();
    }

    private ProductEntity selectProductByName(String name){
        return springData().getEntityManagerByManagedType(ProductEntity.class).createQuery(
                        "select p from ProductEntity p where p.name= :name",
                        ProductEntity.class
                ).setParameter("name", name)
                .getSingleResult();
    }

    private Set<CategoryEntity> selectCategoriesByNames(List<String> categoriesAbsolutePaths){
        var jpql = writeSelectCategoriesByNamesQuery(categoriesAbsolutePaths);

        return new HashSet<>(
                springData().getEntityManagerByManagedType(CategoryEntity.class)
                        .createQuery(jpql, CategoryEntity.class)
                        .getResultList()
        );
    }

    private String writeSelectCategoriesByNamesQuery(List<String> categoriesAbsolutePaths){
        var jpql = new StringBuilder("select c from CategoryEntity c where ");
        var absolutePathsIterator = categoriesAbsolutePaths.listIterator();

        while (absolutePathsIterator.hasNext()){
            var name = absolutePathsIterator.next();
            jpql.append("(concat(c.path, '.', '\"', c.name, '\"')='").append(name).append("')");
            if(absolutePathsIterator.hasNext()){
                jpql.append("or ");
            }
        }
        return jpql.toString();
    }
}
