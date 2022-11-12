package com.shop4me.productdatastream.domain.service.persisting.review;

import com.shop4me.productdatastream.domain.port.persisting.review.ReviewEditingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ReviewEditRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.Map;

import static java.util.Map.entry;

@Slf4j

@AllArgsConstructor
public class ReviewEditingService implements ReviewEditingExecutor {

    private final EntityManager reviewEntityManager;

    @Override
    public Map<String, Integer> execute(@NotNull ReviewEditRequest request){
        log.info(request.toString());
        var jpql = request.writeJpqlQuery();
        var affectedRows = executeJpqlQuery(jpql);
        return Map.ofEntries(
                entry("affected_rows", affectedRows)
        );
    }

    private int executeJpqlQuery(String jpql){
        return reviewEntityManager
                .createQuery(jpql)
                .executeUpdate();
    }

}
