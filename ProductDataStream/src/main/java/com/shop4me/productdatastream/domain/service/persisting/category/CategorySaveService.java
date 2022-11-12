package com.shop4me.productdatastream.domain.service.persisting.category;

import com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus;
import com.shop4me.productdatastream.domain.port.persisting.category.CategorySaveExecutor;
import com.shop4me.productdatastream.domain.port.requesting.CategorySaveRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.Map;

@Slf4j

@AllArgsConstructor
public class CategorySaveService implements CategorySaveExecutor {

    private final EntityManager categoryEntityManager;

    @Override
    public Map<String, String> execute(@NonNull CategorySaveRequest request){
        log.info(request.toString());
        var categorySavingStatusMap = new HashMap<String, String>();
        var categorySaveMap = request.getCategoriesToSaveMap();

        categorySaveMap.keySet().forEach(correlationId ->{
            var category = categorySaveMap.get(correlationId);
            try{
                var absolutePath = category.getAbsolutePath();

                if(checkIfCategoryExistsInGivenPath(absolutePath, request.getTenantId())){
                    categoryEntityManager.persist(category.toDao());
                    categorySavingStatusMap.put(correlationId, ExecutionStatus.SUCCESS.name());
                }
                else {
                    throw new EntityExistsException( category + " already exists...");
                }
            }catch (PersistenceException e){
                log.info("Failed to save category: '{}', correlation id: '{}', reason: '{}'",
                        category,
                        correlationId,
                        e.getMessage()
                );
                categorySavingStatusMap.put(correlationId, ExecutionStatus.FAILURE.name());
            }
        });
        return categorySavingStatusMap;
    }

    private boolean checkIfCategoryExistsInGivenPath(String absolutePath, int tenantId){
        return categoryEntityManager
                .createQuery(
                        "select case when count(c.name)=0 " +
                                "then true else false end from CategoryEntity c " +
                                "where concat(c.path, '.', '\"', c.name, '\"')= :absolutePath " +
                                "and c.tenantId=:tenant",
                        Boolean.class)
                .setParameter("absolutePath", absolutePath)
                .setParameter("tenant", tenantId)
                .getSingleResult();
    }
}
