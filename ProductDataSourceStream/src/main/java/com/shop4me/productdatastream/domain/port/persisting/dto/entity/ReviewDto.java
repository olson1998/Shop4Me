package com.shop4me.productdatastream.domain.port.persisting.dto.entity;

import com.shop4me.productdatastream.domain.port.persisting.dao.ReviewDao;

public interface ReviewDto {

    Long getId();

    long getProductId();

    long getReviewerId();

    ReviewDao toDao();
}
