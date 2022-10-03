package com.shop4me.productdatastream.domain.model.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.port.persisting.dao.CategoryDao;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@AllArgsConstructor
@NoArgsConstructor

public class Category implements CategoryDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("path")
    private String path;

    @JsonProperty("name")
    private String name;

    @Override
    @JsonIgnore
    public CategoryDao toDao() {
        return new CategoryEntity(id, path, name);
    }

    @Override
    @JsonIgnore
    public String getAbsolutePath() {
        return path + ".\"" + name + "\"";
    }

    @Override
    public void setDefaultName() {
        this.name = "\"new category\"";
    }

    @Override
    public void setDefaultPath() {
        this.path = "\"all\"";
    }

    @Override
    @JsonIgnore
    public String toString() {
        return getAbsolutePath();
    }
}
