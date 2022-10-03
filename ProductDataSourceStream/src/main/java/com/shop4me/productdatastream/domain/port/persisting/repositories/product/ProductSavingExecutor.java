package com.shop4me.productdatastream.domain.port.persisting.repositories.product;

import com.shop4me.productdatastream.domain.port.requesting.ProductSaveRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ProductSavingExecutor {

    @Modifying
    @Transactional("productDataStreamTransactionManager")
    Map<String, String> execute(ProductSaveRequest request);
}
