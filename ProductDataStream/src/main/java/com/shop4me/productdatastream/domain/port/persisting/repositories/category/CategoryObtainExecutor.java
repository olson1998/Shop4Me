package com.shop4me.productdatastream.domain.port.persisting.repositories.category;

import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;
import com.shop4me.productdatastream.domain.port.requesting.CategoryObtainRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryObtainExecutor {

    @Transactional("productDataStreamTransactionManager")
    List<CategoryDto> execute(CategoryObtainRequest request);
}
