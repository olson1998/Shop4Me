package com.shop4me.productdatastream.domain.service.persisting.product;

import com.shop4me.productdatastream.domain.port.persisting.repositories.product.ProductSavingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ProductSaveRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

import static com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus.FAILURE;
import static com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus.SUCCESS;

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
                productEntityManager.persist(product.toDao());

                savingStatusMap.put(correlationId, SUCCESS.name());
            }catch (EntityExistsException e){
                log.error("Failed to save product: '{}', reason: {}",
                        correlationId,
                        e.toString()
                );
                savingStatusMap.put(correlationId, FAILURE.name());
            }
        });
        return savingStatusMap;
    }

}
