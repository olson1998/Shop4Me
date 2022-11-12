package com.shop4me.productdatastream.domain.service.persisting.product;

import com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus;
import com.shop4me.productdatastream.domain.port.persisting.product.ProductSavingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ProductSaveRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor

@Slf4j
public class ProductSavingService implements ProductSavingExecutor {

    private final EntityManager productEntityManager;

    @Override
    public Map<String, String> execute(@NonNull ProductSaveRequest request){
        log.info(request.toString());

        var savingStatusMap = new HashMap<String, String>();
        var products = request.getProductsSaveMap();

        products.keySet().forEach(correlationId->{
            var product = products.get(correlationId);
            try{
                var productDao = product.toDao();
                productEntityManager.persist(productDao);

                savingStatusMap.put(correlationId, ExecutionStatus.SUCCESS.name());
            }catch (PersistenceException e){
                log.error("Failed to save product: '{}', reason: {}",
                        correlationId,
                        e.toString()
                );
                savingStatusMap.put(correlationId, ExecutionStatus.FAILURE.name());
            }
        });
        return savingStatusMap;
    }

}
