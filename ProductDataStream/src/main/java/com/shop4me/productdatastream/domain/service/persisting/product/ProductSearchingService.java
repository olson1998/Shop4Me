package com.shop4me.productdatastream.domain.service.persisting.product;

import com.shop4me.productdatastream.domain.port.persisting.product.ProductSearchingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ProductSearchRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;

@AllArgsConstructor

@Slf4j
public class ProductSearchingService implements ProductSearchingExecutor {

    private final EntityManager productEntityManager;

    @Override
    public List<Long> execute(@NonNull ProductSearchRequest request){
        log.info(request.toString());
        var jpql = request.writeJpqlQuery();
        return executeJpqlQuery(jpql);
    }

    private List<Long> executeJpqlQuery(String jpql){
        return productEntityManager
                .createQuery(jpql, Long.class)
                .getResultList();
    }


}
