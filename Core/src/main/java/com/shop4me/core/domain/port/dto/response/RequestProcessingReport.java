package com.shop4me.core.domain.port.dto.response;

import java.util.Map;

public interface RequestProcessingReport {

    String getProcessingStatus();

    String getMsg();

    Map<Object, String> getProcessingDetails();
}
