package com.shop4me.productdatastream.domain.service.persisting.product;

import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductDto;
import com.shop4me.productdatastream.domain.port.persisting.repositories.product.ProductObtainingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ProductObtainRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;

@AllArgsConstructor

@Slf4j
public class ProductObtainingService implements ProductObtainingExecutor {

    private final EntityManager entityManager;

    @Override
    public List<ProductDto> execute(ProductObtainRequest request) {
        log.info(request.toString());
        var jpql = request.writeJpqlQuery();

        return executeJpqlQuery(jpql).stream()
                .map(ProductEntity::toDto)
                .toList();
    }

    private List<ProductEntity> executeJpqlQuery(String jpql){
        return entityManager
                .createQuery(jpql, ProductEntity.class)
                .getResultList();

    }

}
