package com.shop4me.productdatastream.domain.service.persisting.category;

import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;
import com.shop4me.productdatastream.domain.port.persisting.repositories.category.CategoryObtainExecutor;
import com.shop4me.productdatastream.domain.port.requesting.CategoryObtainRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor

@Slf4j
public class CategoryObtainService implements CategoryObtainExecutor {

    private final EntityManager categoryEntityManager;

    @Override
    public List<CategoryDto> execute(@NotNull CategoryObtainRequest request){
        log.info(request.toString());
        logWarnIfRequestContainPayload(request);

        return executeJpql().stream()
                .map(CategoryEntity::toDto)
                .toList();
    }

    private List<CategoryEntity> executeJpql(){
        var jpql = "select c from CategoryEntity c";
        return categoryEntityManager
                .createQuery(jpql, CategoryEntity.class)
                .getResultList();
    }

    private void logWarnIfRequestContainPayload(CategoryObtainRequest request){
        if(request.getPayload() != null){
            log.warn("OBTAIN CATEGORIES payload ignored");
        }
    }
}
