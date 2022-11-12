package com.shop4me.productdatastream.domain.port.persisting.product;

import com.shop4me.productdatastream.domain.port.requesting.ProductSearchRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductSearchingExecutor {

    @Transactional("productDataStreamTransactionManager")
    List<Long> execute(ProductSearchRequest request);
}
