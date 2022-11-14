package com.shop4me.core.domain.service.requesting.productdatastream;

import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.web.datastream.ProductDataStream;
import com.shop4me.core.domain.port.web.datastream.productdatastream.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@AllArgsConstructor

@Service
public class CategoryService implements CategoryRepository {

    private final ProductDataStream productDataStream;

    @Override
    public Mono<Map<String, String>> saveCategories(int tenantId, Map<String, CategoryDto> categorySaveMap){
        return productDataStream.requestSavingCategory(tenantId, categorySaveMap);
    }
}
