package com.shop4me.core.domain.service.requesting.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.web.datastream.ProductDataStream;
import com.shop4me.core.domain.port.web.datastream.productdatastream.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor

@Service
public class CategoryService implements CategoryRepository {

    private final ProductDataStream productDataStream;

    @Override
    public Mono<Map<String, String>> saveCategories(CategoryDto[] categories){
        var categorySaveMap = createSaveCategoryMap(categories);
        return productDataStream.requestSavingCategory(categorySaveMap);
    }

    private Map<String, CategoryDto> createSaveCategoryMap(CategoryDto[] categories){
        var saveMap = new HashMap<String, CategoryDto>();
        Arrays.stream(categories).forEach(category ->{
            var absolutePath = category.getAbsolutePath();
            saveMap.put(absolutePath, category);
        });
        return saveMap;
    }
}
