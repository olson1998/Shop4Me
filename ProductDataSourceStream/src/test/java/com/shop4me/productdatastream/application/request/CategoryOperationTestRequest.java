package com.shop4me.productdatastream.application.request;

import com.shop4me.productdatastream.application.requesting.Shop4MeCoreRequest;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;

import java.util.Map;

import static com.shop4me.productdatastream.application.PayloadWriter.write;
import static com.shop4me.productdatastream.application.request.utils.OperationStrings.OBTAIN;
import static com.shop4me.productdatastream.application.request.utils.OperationStrings.SAVE;

public class CategoryOperationTestRequest {

    public static final String CATEGORY = "CATEGORY";

    public static CoreRequest categorySaveRequest(Map<String, CategoryDto> categoryDtoMap){
        return new Shop4MeCoreRequest(
                CATEGORY,
                SAVE,
                write(categoryDtoMap)
        );
    }

    public static CoreRequest categoryObtainRequest() {
        return new Shop4MeCoreRequest(
                CATEGORY,
                OBTAIN,
                null
        );
    }
}
