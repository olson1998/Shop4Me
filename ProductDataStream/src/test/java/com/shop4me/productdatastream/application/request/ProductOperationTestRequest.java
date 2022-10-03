package com.shop4me.productdatastream.application.request;

import com.shop4me.productdatastream.application.PayloadWriter;
import com.shop4me.productdatastream.application.requesting.Shop4MeCoreRequest;
import com.shop4me.productdatastream.domain.model.data.dto.Product;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductDto;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductSearchFilterDto;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;

import java.util.Map;

import static com.shop4me.productdatastream.application.request.utils.OperationStrings.*;

public class ProductOperationTestRequest {

    public static final String PRODUCT = "PRODUCT";

    public static CoreRequest productObtainCoreRequest(long ... idArray){
        return new Shop4MeCoreRequest(
                PRODUCT,
                OBTAIN,
                PayloadWriter.write(idArray)
        );
    }

    public static CoreRequest productSearchCoreRequest(ProductSearchFilterDto[] productSearchFilterArray){
        return new Shop4MeCoreRequest(
                PRODUCT,
                SEARCH,
                PayloadWriter.write(productSearchFilterArray)
        );
    }

    public static CoreRequest productEditCoreRequest(Map<String, String> productPropertyEditedValueMap){
        return new Shop4MeCoreRequest(
                PRODUCT,
                EDIT,
                PayloadWriter.write(productPropertyEditedValueMap)
        );
    }

    public static CoreRequest productDeleteCoreRequest(Product product){
        return new Shop4MeCoreRequest(
                PRODUCT,
                DELETE,
                PayloadWriter.write(product)
        );
    }

    public static CoreRequest productSaveCoreRequest(Map<String, Product> productSaveMap){
        return new Shop4MeCoreRequest(
                PRODUCT,
                SAVE,
                PayloadWriter.write(productSaveMap)
        );
    }
}
