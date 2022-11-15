package com.shop4me.core.domain.port.web.messaging;

import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;

import java.util.Map;

public interface CategoryTopicListenerRepository {

    String requestSavingCategories(int tenantId, Map<String, CategoryDto> categorySaveMap);

    String requestObtainingCategories(int tenantId);
}
