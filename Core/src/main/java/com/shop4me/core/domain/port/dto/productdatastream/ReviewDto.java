package com.shop4me.core.domain.port.dto.productdatastream;

public interface ReviewDto {

    Long getId();

    Long getProductId();

    Long getReviewerId();

    String getReviewerName();

    byte getPoints();

    String getText();

    String getPublishingDate();

    String savingKey();

}
