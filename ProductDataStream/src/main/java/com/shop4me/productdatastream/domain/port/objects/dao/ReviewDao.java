package com.shop4me.productdatastream.domain.port.objects.dao;

import com.shop4me.productdatastream.domain.port.objects.dto.ReviewDto;

public interface ReviewDao {

    Long getId();

    ReviewDto toDto();
}
