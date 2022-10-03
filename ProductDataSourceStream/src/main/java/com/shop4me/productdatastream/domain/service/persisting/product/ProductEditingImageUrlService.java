package com.shop4me.productdatastream.domain.service.persisting.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

@Slf4j

@RequiredArgsConstructor
public class ProductEditingImageUrlService {

    private final ObjectMapper mapper;

    public int edit(ProductEntity product, String imageUrlEditValue){
        try{
            return 1;
        }catch (Exception e){
            log.warn("Could not edit product: {} image urls, reason: {}", product.getId(), e.toString());
            return 0;
        }
    }
}
