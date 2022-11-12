package com.shop4me.productdatastream.domain.port.persisting.product;

import com.shop4me.productdatastream.domain.port.requesting.ProductEditRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ProductEditingExecutor {

    @Modifying
    @Transactional("productDataStreamTransactionManager")
    Map<String, String> execute(ProductEditRequest request);
}
