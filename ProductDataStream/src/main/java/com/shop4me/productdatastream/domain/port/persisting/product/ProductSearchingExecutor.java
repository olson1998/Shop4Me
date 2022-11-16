package com.shop4me.productdatastream.domain.port.persisting.product;

import com.shop4me.productdatastream.domain.port.requesting.ProductMultiSearchRequest;
import com.shop4me.productdatastream.domain.port.requesting.ProductSearchRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

public interface ProductSearchingExecutor {

    @Transactional("productDataStreamTransactionManager")
    List<Long> execute(ProductSearchRequest request);

    @Transactional("productDataStreamTransactionManager")
    MultiValueMap<String, Long> execute(ProductMultiSearchRequest request);
}
