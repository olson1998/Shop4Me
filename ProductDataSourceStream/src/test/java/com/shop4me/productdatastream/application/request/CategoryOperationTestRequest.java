package com.shop4me.productdatastream.application.request;

import com.shop4me.productdatastream.application.PayloadWriter;
import com.shop4me.productdatastream.application.requesting.Shop4MeCoreRequest;
import com.shop4me.productdatastream.domain.model.data.dto.Category;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;

import java.util.Map;

import static com.shop4me.productdatastream.application.request.utils.OperationStrings.OBTAIN;
import static com.shop4me.productdatastream.application.request.utils.OperationStrings.SAVE;

public class CategoryOperationTestRequest {

    public static final String CATEGORY = "CATEGORY";

    public static CoreRequest categorySaveCoreRequest(Map<String, Category> categoryDtoMap){
        return new Shop4MeCoreRequest(
                CATEGORY,
                SAVE,
                PayloadWriter.write(categoryDtoMap)
        );
    }

    public static CoreRequest categoryObtainCoreRequest() {
        return new Shop4MeCoreRequest(
                CATEGORY,
                OBTAIN,
                null
        );
    }
}
