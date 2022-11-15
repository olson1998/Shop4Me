package com.shop4me.core.application.dto.productdatastream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.productdatastream.utils.ProductSearchFilterDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor
public class SearchFilter implements ProductSearchFilterDto {

    @JsonProperty("property")
    private String property;

    @JsonProperty("operator")
    private String operator;

    @JsonProperty("value")
    private String value;
}
