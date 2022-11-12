package com.shop4me.productdatastream.domain.port.objects.dao;

import com.shop4me.productdatastream.domain.port.objects.dto.ImageUrlDto;

public interface ImageUrlDao {

    String getId();

    ImageUrlDto toDto();
}
