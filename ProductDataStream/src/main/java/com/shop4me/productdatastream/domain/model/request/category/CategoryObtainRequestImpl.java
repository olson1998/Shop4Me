package com.shop4me.productdatastream.domain.model.request.category;

import com.shop4me.productdatastream.domain.port.requesting.CategoryObtainRequest;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor

public class CategoryObtainRequestImpl implements CategoryObtainRequest {

    @Getter
    private String payload;

    @Override
    public String toString() {
        return "OBTAIN ALL CATEGORIES";
    }

    public static CategoryObtainRequestImpl fromCoreRequest(@NonNull CoreRequest request){
        var payload = request.getPayload();
        if(payload != null){
            payload = request.decodePayload().getPayload();
            return new CategoryObtainRequestImpl(payload);
        }
        else {
            return new CategoryObtainRequestImpl();
        }
    }
}
