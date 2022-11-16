package com.shop4me.core.application.dto.productdatastream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor
@AllArgsConstructor
public class Category implements CategoryDto {

    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "path")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String path;

    @Override
    @JsonIgnore
    public String getAbsolutePath() {
        return path + ".\"" + name + "\"";
    }

    @Override
    @JsonIgnore
    public String toString() {
        return getAbsolutePath();
    }

    public static Category fromDto(CategoryDto categoryDto){
        return new Category(
                categoryDto.getId(),
                categoryDto.getName(),
                categoryDto.getPath()
        );
    }
}
