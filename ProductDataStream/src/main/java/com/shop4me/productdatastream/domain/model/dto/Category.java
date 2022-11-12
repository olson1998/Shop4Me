package com.shop4me.productdatastream.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.port.objects.dao.CategoryDao;
import com.shop4me.productdatastream.domain.port.objects.dto.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@AllArgsConstructor
@NoArgsConstructor

public class Category implements CategoryDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty(value = "tenant", required = true, access = JsonProperty.Access.WRITE_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int tenantId;

    @JsonProperty("path")
    private String path;

    @JsonProperty("name")
    private String name;

    @Override
    @JsonIgnore
    public CategoryDao toDao() {
        return new CategoryEntity(id, tenantId, path, name);
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
    public void setTenantId(int tenantId) {
        this.tenantId=tenantId;
    }

    @Override
    @JsonIgnore
    public String toString() {
        return getAbsolutePath();
    }

    public Category(int tenantId, String path, String name) {
        this.tenantId = tenantId;
        this.path = path;
        this.name = name;
    }
}
