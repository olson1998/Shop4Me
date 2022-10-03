package com.shop4me.productdatastream.domain.model.request.category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shop4me.productdatastream.domain.model.data.dto.Category;
import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.model.request.toolset.RequestPayloadReader;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;
import com.shop4me.productdatastream.domain.port.requesting.CategorySaveRequest;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor

public class CategorySaveRequestImpl implements CategorySaveRequest {

    @Getter
    private Map<String, CategoryDto> categoriesToSaveMap;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("SAVE CATEGORY: (");
        appendValues(str);
        str.append(")");
        return str.toString();
    }

    private void appendValues(StringBuilder str){
        var categories = getCategoriesToSave();

        var size = categories.size();
        var last = size - 1;
        for(int i =0; i < size; i++){
            var c = categories.get(i);
            var name = c.getName();
            var path = c.getPath();

            if(name ==null){
                name = "?";
            }
            str.append("(NAME: '").append(name).append("', PATH: '");
            if(path == null){
                str.append("\"all\"'");
            }
            else {
                str.append(path).append("'");
            }
            str.append(")");
            if(i < last){
                str.append(", ");
            }
        }
    }

    private List<CategoryDto> getCategoriesToSave(){
        return categoriesToSaveMap.values().stream()
                .sorted(Comparator.comparing(CategoryDto::getAbsolutePath))
                .toList();
    }

    @SneakyThrows
    public static CategorySaveRequestImpl fromCoreRequest(CoreRequest request){
        var payload = request.decodePayload().getPayload();
        EmptyPayloadCheck.scan(payload, "{}");

        var categoriesMap = RequestPayloadReader.OBJECT_MAPPER.readValue(
                payload, new TypeReference<Map<String, Category>>() {}
        );

        var categoriesDtoMap = writeCategoryDtoSaveMap(categoriesMap);

        return new CategorySaveRequestImpl(categoriesDtoMap);
    }

    private static Map<String, CategoryDto> writeCategoryDtoSaveMap(Map<String, Category> categoryMap){
        var categoryDtoMap = new HashMap<String, CategoryDto>();

        categoryMap.keySet().forEach(correlationId -> {
            var category = categoryMap.get(correlationId);

            if(category.getName() == null){category.setDefaultName();}
            if(category.getPath() == null){category.setDefaultPath();}

            categoryDtoMap.put(correlationId, category);
        });
        return categoryDtoMap;
    }
}
