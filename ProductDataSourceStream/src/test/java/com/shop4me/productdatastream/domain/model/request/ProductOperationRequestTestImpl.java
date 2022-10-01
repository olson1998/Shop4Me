package com.shop4me.productdatastream.domain.model.request;

import com.shop4me.productdatastream.domain.model.request.product.*;
import com.shop4me.productdatastream.domain.model.request.product.tools.ProductSearchFilter;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductDto;
import com.shop4me.productdatastream.domain.port.requesting.*;

import java.util.Map;

public class ProductOperationRequestTestImpl {

    public static ProductObtainRequest productObtainRequest(long[] idArray){
        return new ProductObtainRequestImpl(idArray);
    }

    public static ProductSaveRequest productSaveRequest(Map<String, ProductDto> productSaveMap){
        return new ProductSaveRequestImpl(productSaveMap);
    }

    public static ProductDeleteRequest productDeleteRequest(ProductDto productDto){
        return new ProductDeleteRequestImpl(productDto);
    }

    public static ProductEditRequest productEditRequest(Map<String, String> productEditingPropertiesMap){
        return new ProductEditRequestImpl(productEditingPropertiesMap);
    }

    public static ProductSearchRequest productSearchRequest(ProductSearchFilter[] productSearchFilters){
        return new ProductSearchRequestImpl(productSearchFilters);
    }
}
