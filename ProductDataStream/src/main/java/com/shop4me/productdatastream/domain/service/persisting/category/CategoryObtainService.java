package com.shop4me.productdatastream.domain.service.persisting.category;

import com.shop4me.productdatastream.domain.port.persisting.category.CategoryObtainExecutor;
import com.shop4me.productdatastream.domain.port.requesting.CategoryObtainRequest;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.port.objects.dto.CategoryDto;
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

        var jpql = request.writeJpql();
        return categoryEntityManager
                .createQuery(jpql, CategoryEntity.class)
                .getResultList()
                .stream()
                .map(CategoryEntity::toDto)
                .toList();
    }

}
