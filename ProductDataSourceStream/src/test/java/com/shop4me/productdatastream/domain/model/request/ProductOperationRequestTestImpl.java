package com.shop4me.productdatastream.domain.model.request;

import com.shop4me.productdatastream.domain.model.request.product.ProductObtainRequestImpl;
import com.shop4me.productdatastream.domain.port.requesting.ProductObtainRequest;

public class ProductOperationRequestTestImpl {

    public static ProductObtainRequest productObtainRequest(long[] idArray){
        return new ProductObtainRequestImpl(idArray);
    }
}
