package com.shop4me.core.application.config.datastream.productdatastream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.port.web.client.DataStreamWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor

@Configuration
public class ProductDataStreamConfig {

    private final DataStreamWebClient productDataStreamWebClient;

    private final ObjectMapper mapper;


}
