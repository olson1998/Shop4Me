package com.shop4me.productdatastream.domain.model.request;

import com.shop4me.productdatastream.domain.model.request.category.CategorySaveRequestImpl;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;
import com.shop4me.productdatastream.domain.port.requesting.CategorySaveRequest;

import java.util.Map;

public class CategoryOperationRequestTestImpl {

    public static CategorySaveRequest categorySaveRequest(Map<String, CategoryDto> savingCategoryMap){
        return new CategorySaveRequestImpl(savingCategoryMap);
    }
}
