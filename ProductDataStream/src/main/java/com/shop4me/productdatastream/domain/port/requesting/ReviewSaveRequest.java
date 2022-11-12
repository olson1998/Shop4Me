package com.shop4me.productdatastream.domain.port.requesting;

import com.shop4me.productdatastream.domain.port.objects.dto.ReviewDto;

import java.util.Map;

public interface ReviewSaveRequest {

    Map<String, ReviewDto> getRequestedToSave();
}
