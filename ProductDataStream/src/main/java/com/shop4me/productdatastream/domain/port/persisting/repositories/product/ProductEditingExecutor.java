package com.shop4me.productdatastream.domain.port.persisting.repositories.product;

import com.shop4me.productdatastream.domain.port.requesting.ProductEditRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ProductEditingExecutor {

    @Modifying
    @Transactional("productDataStreamTransactionManager")
    Map<String, Integer> execute(ProductEditRequest request);
}
