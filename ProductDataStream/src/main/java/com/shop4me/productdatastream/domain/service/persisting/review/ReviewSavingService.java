package com.shop4me.productdatastream.domain.service.persisting.review;

import com.shop4me.productdatastream.domain.model.request.enumset.ExecutionStatus;
import com.shop4me.productdatastream.domain.port.persisting.review.ReviewSavingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ReviewSaveRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor

@Slf4j
public class ReviewSavingService implements ReviewSavingExecutor {

    private final EntityManager reviewEntityManager;

    @Override
    public Map<String, String> execute(@NotNull ReviewSaveRequest request){
        log.info(request.toString());
        var savingStatusMap = new HashMap<String, String>();
        var requestedToSave = request.getRequestedToSave();
        requestedToSave.keySet().forEach(correlationId->{
            var review = requestedToSave.get(correlationId);
            try{
                reviewEntityManager.persist(review.toDao());
                savingStatusMap.put(correlationId, ExecutionStatus.SUCCESS.name());
            }catch (EntityExistsException e){
                log.info("Failed to save Review: {product id: '{}', reviewer id: '{}'}, correlation id: '{}', reason: '{}'",
                        review.getProductId(),
                        review.getReviewerId(),
                        correlationId,
                        e.getMessage()
                );
                savingStatusMap.put(correlationId, ExecutionStatus.FAILURE.name());
            }
        });
        return savingStatusMap;
    }

}
