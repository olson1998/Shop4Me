package com.shop4me.productdatastream.domain.port.objects.dto;

import com.shop4me.productdatastream.domain.port.objects.dao.ImageUrlDao;

public interface ImageUrlDto {

    String getId();

    long getProductId();

    String getUrl();

    Boolean getVisibility();

    ImageUrlDao toDao();
}
