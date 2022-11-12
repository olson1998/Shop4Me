package com.shop4me.productdatastream.domain.port.persisting.review;

import com.shop4me.productdatastream.domain.port.requesting.ReviewEditRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ReviewEditingExecutor {

    @Modifying
    @Transactional("productDataStreamTransactionManager")
    Map<String, Integer> execute(ReviewEditRequest request);
}
