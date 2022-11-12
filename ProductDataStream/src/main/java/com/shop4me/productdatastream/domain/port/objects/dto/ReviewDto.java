package com.shop4me.productdatastream.domain.port.objects.dto;

import com.shop4me.productdatastream.domain.port.objects.dao.ReviewDao;

public interface ReviewDto {

    Long getId();

    long getProductId();

    long getReviewerId();

    ReviewDao toDao();
}
