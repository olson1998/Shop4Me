package com.shop4me.productdatastream.domain.port.persisting.category;

import com.shop4me.productdatastream.domain.port.requesting.CategorySaveRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface CategorySaveExecutor {

    @Modifying
    @Transactional("productDataStreamTransactionManager")
    Map<String, String> execute(CategorySaveRequest request);
}
