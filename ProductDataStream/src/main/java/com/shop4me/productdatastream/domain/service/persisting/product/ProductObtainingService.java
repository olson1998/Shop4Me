package com.shop4me.productdatastream.domain.service.persisting.product;

import com.shop4me.productdatastream.domain.model.dao.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.port.persisting.product.ProductObtainingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ProductObtainRequest;
import com.shop4me.productdatastream.domain.port.objects.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.List;

@AllArgsConstructor

@Slf4j
public class ProductObtainingService implements ProductObtainingExecutor {

    private final EntityManager entityManager;

    @Override
    public List<ProductDto> execute(@NotNull ProductObtainRequest request) {
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
