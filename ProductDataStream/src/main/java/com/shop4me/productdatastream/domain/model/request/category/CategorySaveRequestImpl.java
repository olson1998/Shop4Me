package com.shop4me.productdatastream.domain.model.request.category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shop4me.productdatastream.domain.model.dto.Category;
import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.model.request.utils.RequestPayloadReader;
import com.shop4me.productdatastream.domain.port.messaging.InboundMsg;
import com.shop4me.productdatastream.domain.port.objects.dto.CategoryDto;
import com.shop4me.productdatastream.domain.port.requesting.CategorySaveRequest;
import lombok.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)

public class CategorySaveRequestImpl implements CategorySaveRequest {

    private final Map<String, CategoryDto> categoriesToSaveMap;

    private final int tenantId;

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
    public static CategorySaveRequestImpl fromInboundMessage(@NonNull InboundMsg inboundMsg){
        var payload = inboundMsg.getDecodedPayload();
        EmptyPayloadCheck.scan(payload, "{}");

        var categoriesMap = RequestPayloadReader.OBJECT_MAPPER.readValue(
                payload, new TypeReference<Map<String, Category>>() {}
        );

        var categoriesDtoMap = writeCategoryDtoSaveMap(categoriesMap, inboundMsg.getTenantId());

        return new CategorySaveRequestImpl(categoriesDtoMap, inboundMsg.getTenantId());
    }

    private static Map<String, CategoryDto> writeCategoryDtoSaveMap(Map<String, Category> categoryMap, int tenantId){
        var categoryDtoMap = new HashMap<String, CategoryDto>();

        categoryMap.keySet().forEach(correlationId -> {
            var category = categoryMap.get(correlationId);
            category.setTenantId(tenantId);

            if(category.getName() == null){category.setDefaultName();}
            if(category.getPath() == null){category.setDefaultPath();}

            categoryDtoMap.put(correlationId, category);
        });
        return categoryDtoMap;
    }
}
