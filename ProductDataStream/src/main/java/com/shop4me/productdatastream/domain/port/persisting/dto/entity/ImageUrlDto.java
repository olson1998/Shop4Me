package com.shop4me.productdatastream.domain.port.persisting.dto.entity;

import com.shop4me.productdatastream.domain.port.persisting.dao.ImageUrlDao;

public interface ImageUrlDto {

    String getId();

    long getProductId();

    String getUrl();

    Boolean getVisibility();

    ImageUrlDao toDao();
}
