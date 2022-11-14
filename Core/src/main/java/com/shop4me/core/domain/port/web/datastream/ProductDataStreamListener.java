package com.shop4me.core.domain.port.web.datastream;

import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;

import java.util.concurrent.CompletableFuture;

public interface ProductDataStreamListener {

    CompletableFuture<ProductDto> requestObtainingProduct(long[] productIds);
}
