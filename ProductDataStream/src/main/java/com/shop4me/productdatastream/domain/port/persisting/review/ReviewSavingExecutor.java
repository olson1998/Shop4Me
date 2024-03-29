package com.shop4me.productdatastream.domain.port.persisting.review;

import com.shop4me.productdatastream.domain.port.requesting.ReviewSaveRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ReviewSavingExecutor {

    @Modifying
    @Transactional("productDataStreamTransactionManager")
    Map<String, String> execute(ReviewSaveRequest request);
}
