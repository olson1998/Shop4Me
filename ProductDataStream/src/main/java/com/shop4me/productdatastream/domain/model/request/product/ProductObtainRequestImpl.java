package com.shop4me.productdatastream.domain.model.request.product;

import com.shop4me.productdatastream.domain.model.request.toolset.RequestPayloadReader;
import com.shop4me.productdatastream.domain.model.request.EmptyPayloadCheck;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.ProductObtainRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class ProductObtainRequestImpl implements ProductObtainRequest {

    private long[] requestedIds;

    @Override
    public String writeJpqlQuery() {
        StringBuilder query = new StringBuilder("select p from ProductEntity p ");
        if (requestedIds.length > 0) {
            query.append("where ");
            int size = requestedIds.length;
            var idList = arrayToList(requestedIds);
            var iterator = idList.listIterator();
            while (iterator.hasNext()) {
                query.append("p.id=").append(iterator.next()).append(" ");
                if (iterator.nextIndex() < size) {
                    query.append("or ");
                }
            }
        }
        return query.toString();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("OBTAIN PRODUCT WITH ID:(");
        int size = requestedIds.length;
        int last = size-1;
        for(int i = 0; i< size; i++){
            str.append(requestedIds[i]);
            if(i < last){
                str.append(", ");
            }
        }
        str.append(")");
        return str.toString();
    }

    private List<Long> arrayToList(long[] array){
        return Arrays.stream(array).boxed()
                .toList();
    }

    @SneakyThrows
    public static ProductObtainRequestImpl fromCoreRequest(@NonNull CoreRequest request){
        var json = request.decodePayload().getPayload();

        EmptyPayloadCheck.scan(json, "[]");

        var requested = RequestPayloadReader.OBJECT_MAPPER.readValue(json, long[].class);

        return new ProductObtainRequestImpl(requested);
    }

}
