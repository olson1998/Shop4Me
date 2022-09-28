package com.shop4me.productdatastream.domain.port.persisting.repositories.product;

import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductDto;
import com.shop4me.productdatastream.domain.port.requesting.ProductObtainRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductObtainingExecutor {

    @Transactional("productDbTransactionManager")
    List<ProductDto> execute(ProductObtainRequest request);
}
