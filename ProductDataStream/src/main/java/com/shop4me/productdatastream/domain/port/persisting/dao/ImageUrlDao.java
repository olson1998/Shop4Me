package com.shop4me.productdatastream.domain.port.persisting.dao;

import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ImageUrlDto;

public interface ImageUrlDao {

    String getId();

    ImageUrlDto toDto();
}
