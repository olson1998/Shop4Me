package com.shop4me.productdatastream.domain.service.persisting.product;

import com.shop4me.productdatastream.domain.port.persisting.product.ProductDeletingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ProductDeleteRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.Map;

import static java.util.Map.entry;

@Slf4j

@AllArgsConstructor

public class ProductDeletingService implements ProductDeletingExecutor {

    private final EntityManager productEntityManager;

    @Override
    public Map<String, Integer> execute(@NonNull ProductDeleteRequest deleteRequest){
        log.info(deleteRequest.toString());

        var jpql = deleteRequest.writeJpqlQuery();
        var affectedRows = executeJpql(productEntityManager, jpql);
        return Map.ofEntries(
                entry("affected_rows", affectedRows)
        );
    }

    private int executeJpql(EntityManager entityManager, String jpql){
        return entityManager.createQuery(jpql)
                .executeUpdate();
    }
}
