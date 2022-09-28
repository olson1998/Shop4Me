package com.shop4me.productdatastream.domain.model.request.review;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shop4me.productdatastream.domain.model.request.toolset.RequestPayloadReader;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.review.ReviewProperty;
import com.shop4me.productdatastream.domain.model.exception.EmptyValueException;
import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.model.request.review.tools.ReviewPropertyComparator;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.ReviewEditRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.review.ReviewProperty.*;

@AllArgsConstructor

public class ReviewEditRequestImpl implements ReviewEditRequest {

    private final Map<ReviewProperty, String> editMap;

    @Override
    public String writeJpqlQuery(){
        var id = editMap.get(ID);
        var query = new StringBuilder("update ReviewEntity r set ");
        writeSet(query);
        query.append(" where r.id=").append(id);
        return query.toString();
    }

    @Override
    public String toString() {
        var id = editMap.get(ID);
        var str = new StringBuilder("EDIT REVIEW WITH ID= '");
        str.append(id).append("' CHANGED:(");
        appendEditedProps(str);
        str.append(")");
        return str.toString();
    }

    private void writeSet(StringBuilder query){
        var comparator = new ReviewPropertyComparator();
        var propertyList = new ArrayList<>(editMap.keySet());
        propertyList.remove(ID);
        propertyList.sort(comparator);
        var size = propertyList.size();
        var last = size -1;
        for(int i =0; i < size; i++){
            var prop = propertyList.get(i);
            if(!prop.equals(ID)){
                if(prop.equals(TEXT)){
                    query.append("r.text='");
                    query.append(editMap.get(TEXT)).append("'");
                }else if(prop.equals(REVIEWER_NAME)){
                    query.append("r.reviewerName='");
                    query.append(editMap.get(REVIEWER_NAME)).append("'");
                }
                if(i < last){
                    query.append(", ");
                }
            }
        }
    }

    private void appendEditedProps(StringBuilder str){
        Set<ReviewProperty> propSet = editMap.keySet();
        ReviewPropertyComparator comparator = new ReviewPropertyComparator();
        List<ReviewProperty> propertyList = new ArrayList<>(List.copyOf(propSet));
        propertyList.sort(comparator);
        propertyList.remove(ID);
        int size = propertyList.size();
        int last = size - 1;
        for(int i = 0; i< size; i++){
            ReviewProperty prop = propertyList.get(i);
            if(!prop.equals(ID)){
                str.append(prop);
                if(i < last){
                    str.append(", ");
                }
            }
        }
    }

    @SneakyThrows
    public static ReviewEditRequestImpl fromCoreRequest(CoreRequest request){
        var json = request.decodePayload().getPayload();

        EmptyPayloadCheck.scan(json, "{}");

        var editMap= RequestPayloadReader.OBJECT_MAPPER.readValue(json, new TypeReference<Map<ReviewProperty, String>>() {});

        throwEmptyValueExceptionIfMissingValue(editMap);
        return new ReviewEditRequestImpl(editMap);
    }

    private static void throwEmptyValueExceptionIfMissingValue(Map<ReviewProperty, String> editMap){
        var corruptedRequests = new ArrayList<ReviewProperty>();
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

    private static String list(List<ReviewProperty> corruptedRequests){
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
}
