package com.shop4me.productdatastream.domain.model.request.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestPayloadReader {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
}
