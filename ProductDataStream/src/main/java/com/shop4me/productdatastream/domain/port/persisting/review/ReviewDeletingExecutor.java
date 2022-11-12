package com.shop4me.productdatastream.domain.port.persisting.review;

import com.shop4me.productdatastream.domain.port.requesting.ReviewDeleteRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ReviewDeletingExecutor {

    @Modifying
    @Transactional("productDataStreamTransactionManager")
    Map<String, Integer> execute(ReviewDeleteRequest request);
}
