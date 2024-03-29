package com.shop4me.productdatastream.domain.port.persisting.product;

import com.shop4me.productdatastream.domain.port.requesting.ProductDeleteRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ProductDeletingExecutor {

    @Modifying
    @Transactional("productDataStreamTransactionManager")
    Map<String, Integer> execute(ProductDeleteRequest request);
}
