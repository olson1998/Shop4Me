package com.shop4me.productdatastream.domain.service.persisting.review;

import com.shop4me.productdatastream.domain.port.persisting.review.ReviewDeletingExecutor;
import com.shop4me.productdatastream.domain.port.requesting.ReviewDeleteRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.Map;

import static java.util.Map.entry;

@Slf4j

@AllArgsConstructor
public class ReviewDeletingService implements ReviewDeletingExecutor {

    private final EntityManager reviewEntityManager;

    @Override
    public Map<String, Integer> execute(@NotNull ReviewDeleteRequest request){
        log.info(request.toString());
        var jpql = request.writeJpqlQuery();
        var affectedRows = executeJpql(jpql);
        return Map.ofEntries(
                entry("affected_rows", affectedRows)
        );
    }

    private int executeJpql(String jpql){
        return reviewEntityManager
                .createQuery(jpql)
                .executeUpdate();
    }
}
