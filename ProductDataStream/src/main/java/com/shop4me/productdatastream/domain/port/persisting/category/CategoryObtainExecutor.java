package com.shop4me.productdatastream.domain.port.persisting.category;

import com.shop4me.productdatastream.domain.port.requesting.CategoryObtainRequest;
import com.shop4me.productdatastream.domain.port.objects.dto.CategoryDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryObtainExecutor {

    @Transactional("productDataStreamTransactionManager")
    List<CategoryDto> execute(CategoryObtainRequest request);
}
