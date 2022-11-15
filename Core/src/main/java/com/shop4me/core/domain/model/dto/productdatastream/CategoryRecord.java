package com.shop4me.core.domain.model.dto.productdatastream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRecord implements CategoryDto {

    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(value = "path")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String path;

    @Override
    @JsonIgnore
    public String getAbsolutePath() {
        return path + ".\"" + name + "\"";
    }
}
