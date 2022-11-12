package com.shop4me.productdatastream.domain.port.persisting.product;

import com.shop4me.productdatastream.domain.port.requesting.ProductObtainRequest;
import com.shop4me.productdatastream.domain.port.objects.dto.ProductDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductObtainingExecutor {

    @Transactional("productDataStreamTransactionManager")
    List<ProductDto> execute(ProductObtainRequest request);
}
