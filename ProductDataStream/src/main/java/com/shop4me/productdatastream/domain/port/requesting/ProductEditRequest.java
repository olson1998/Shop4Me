package com.shop4me.productdatastream.domain.port.requesting;

import java.util.Map;

public interface ProductEditRequest {

    String writeJpqlQuery();

    Map<String, String> getEditMapCopy();

    int getTenantId();

}
