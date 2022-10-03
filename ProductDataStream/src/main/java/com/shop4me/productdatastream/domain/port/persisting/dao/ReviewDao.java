package com.shop4me.productdatastream.domain.port.persisting.dao;

import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ReviewDto;

public interface ReviewDao {

    Long getId();

    ReviewDto toDto();
}
