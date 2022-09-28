package com.shop4me.productdatastream.domain.model.request.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shop4me.productdatastream.domain.model.request.toolset.RequestPayloadReader;
import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.model.request.product.tools.ProductPropertyComparator;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.product.ProductProperty;
import com.shop4me.productdatastream.domain.model.exception.EmptyValueException;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.ProductEditRequest;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;

import static com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.product.ProductProperty.*;

public class ProductEditRequestImpl implements ProductEditRequest {

    private final Map<ProductProperty, String> editMap;

    @Getter
    private final Map<String, String> editMapCopy;

    @Override
    public String writeJpqlQuery(){
        var id = editMap.get(ID);
        var query = new StringBuilder("update ProductEntity p set ");
        writeSet(query);
        query.append(" where p.id=").append(id);
        return query.toString();
    }

    @Override
    public String toString() {
        var id = editMap.get(ID);
        var str = new StringBuilder("EDIT PRODUCT WITH ID= '");
        str.append(id).append("' CHANGED:(");
        appendEditedProps(str);
        str.append(")");
        return str.toString();
    }

    private void writeSet(StringBuilder query){
        var comparator = new ProductPropertyComparator();
        var propertyList = new ArrayList<>(editMap.keySet());
        propertyList.remove(ID);
        propertyList.remove(CATEGORY);
        propertyList.remove(PHOTO_URL);
        propertyList.sort(comparator);
        var size = propertyList.size();
        var last = size -1;
        for(int i =0; i < size; i++){
            var prop = propertyList.get(i);
            var val = editMap.get(prop);
            switch (prop){
                case NAME -> {
                    query.append("p.name=");
                }
                case DESCRIPTION -> {
                    query.append("p.description= ");
                }
            }
            query.append("'").append(val).append("'");
            if(i < last){
                query.append(", ");
            }
        }
    }

    private void appendEditedProps(StringBuilder str){
        Set<ProductProperty> propSet = editMap.keySet();
        ProductPropertyComparator comparator = new ProductPropertyComparator();
        List<ProductProperty> propertyList = new ArrayList<>(List.copyOf(propSet));
        propertyList.remove(ID);
        propertyList.sort(comparator);
        int size = propertyList.size();
        int last = size - 1;
        for(int i = 0; i< size; i++){
            ProductProperty prop = propertyList.get(i);
            if(!prop.equals(ID)){
                str.append(prop);
                if(i < last){
                    str.append(", ");
                }
            }
        }
    }

    @SneakyThrows
    public static ProductEditRequestImpl fromCoreRequest(CoreRequest request){
        var payload = request.decodePayload().getPayload();

        EmptyPayloadCheck.scan(payload, "{}");

        var editMap= RequestPayloadReader.OBJECT_MAPPER.readValue(
                payload,
                new TypeReference<Map<String, String>>() {}
        );
        throwEmptyArgumentsIfMissingValue(editMap);
        return new ProductEditRequestImpl(editMap);
    }

    private static void throwEmptyArgumentsIfMissingValue(Map<String, String> editMap){
        var corruptedRequests = new ArrayList<String>();
        editMap.keySet().forEach(prop->{
            var val = editMap.get(prop);
            if(val == null || val.equals("")){
                corruptedRequests.add(prop);
            }
        });
        if(corruptedRequests.size()>0){
            throw new EmptyValueException(list(corruptedRequests));
        }
    }

    private static String list(List<String> corruptedRequests){
        StringBuilder sb = new StringBuilder("(");
        var size = corruptedRequests.size();
        var last = size - 1;
        for(int i = 0; i < size; i++){
            var property = corruptedRequests.get(i);
            sb.append(property)
                    .append("= '?'");
            if(i < last){
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public ProductEditRequestImpl(Map<String, String> editMap) {
        this.editMap = readProductProperties(editMap);
        this.editMapCopy = editMap;
    }

    private Map<ProductProperty, String> readProductProperties(Map<String, String> editMap){
        var editMapWithProductProperty = new HashMap<ProductProperty, String>();

        editMap.keySet().forEach(propertyStr -> {
            var productProperty = ProductProperty.valueOf(propertyStr);
            var value = editMap.get(propertyStr);

            editMapWithProductProperty.put(productProperty, value);
        });
        return editMapWithProductProperty;
    }
}
