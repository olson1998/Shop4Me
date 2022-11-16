package com.shop4me.productdatastream.domain.service.persisting.product;

import com.shop4me.productdatastream.domain.port.persisting.product.ProductSearchingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ProductMultiSearchRequest;
import com.shop4me.productdatastream.domain.port.requesting.ProductSearchRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor

@Slf4j
public class ProductSearchingService implements ProductSearchingExecutor {

    private final EntityManager productEntityManager;

    @Override
    public List<Long> execute(@NonNull ProductSearchRequest request){
        log.info(request.toString());
        var jpql = request.writeJpqlQuery();
        return productEntityManager
                .createQuery(jpql, Long.class)
                .getResultList();
    }

    @Override
    public MultiValueMap<String, Long> execute(@NonNull ProductMultiSearchRequest request) {
        var resultMap = new LinkedMultiValueMap<String, Long>();
        request.getMultiSearchMap().forEach((correlationId, singleRequest)->{
            var listOfIds = execute(singleRequest);
            resultMap.put(correlationId, listOfIds);
        });
        return resultMap;
    }

}
