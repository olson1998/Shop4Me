package com.shop4me.core.model.dto.productdatastream;

import com.shop4me.core.domain.model.dto.productdatastream.ProductRecord;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;

public class ProductGenerator {

    public static ProductDto generate(String name, String description){
        return new ProductRecord();
    }

    public static ProductDto generate(String name){
        return generate(name, null);
    }
}
