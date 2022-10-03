package com.shop4me.productdatastream.domain.port.requesting;

public interface CoreRequest {

    String getEntity();

    String getOperation();

    String getPayload();

    CoreRequest decodePayload();
}
