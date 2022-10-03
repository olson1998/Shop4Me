package com.shop4me.productdatastream.domain.service.persisting.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j

@RequiredArgsConstructor
public class ProductEditingCategoriesService {

    private final ObjectMapper mapper;

    public int edit(ProductEntity product, String editMapCategoryValue){
        try{
            var readIdArray = mapper.readValue(editMapCategoryValue, long[].class);
            var mergeCategoriesSet = createCategoryEntityList(readIdArray);

            product.setCategoriesSet(mergeCategoriesSet);
            return 1;
        }catch (Exception e){
            log.warn("Could not merge product's categories, reason: {}", e.toString());
            return 0;
        }
    }

    private Set<CategoryEntity> createCategoryEntityList(long[] ids){
        return Arrays.stream(ids)
                .mapToObj(CategoryEntity::new)
                .collect(Collectors.toSet());
    }
}
