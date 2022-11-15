package com.shop4me.core.domain.model.request.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.productdatastream.utils.ProductSearchFilterDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter

@AllArgsConstructor
public class ProductSearchFilter implements ProductSearchFilterDto {

    @JsonProperty("property")
    private String property;

    @JsonProperty("operator")
    private String operator;

    @JsonProperty("value")
    private String value;

    public static ProductSearchFilter fromDto(@NonNull ProductSearchFilterDto dto){
        return new ProductSearchFilter(
                dto.getProperty(),
                dto.getOperator(),
                dto.getValue()
        );
    }

    @Override
    public String toString() {
        return String.format("{%s %s '%s'}", property, operator, value);
    }
}
