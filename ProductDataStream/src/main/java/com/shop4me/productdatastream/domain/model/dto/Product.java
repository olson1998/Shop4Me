package com.shop4me.productdatastream.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.port.objects.dao.ProductDao;
import com.shop4me.productdatastream.domain.port.objects.dto.CategoryDto;
import com.shop4me.productdatastream.domain.port.objects.dto.ImageUrlDto;
import com.shop4me.productdatastream.domain.port.objects.dto.ProductDto;
import com.shop4me.productdatastream.domain.port.objects.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter

@NoArgsConstructor
@AllArgsConstructor

public class Product implements ProductDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty(value = "tenant", required = true, access = JsonProperty.Access.WRITE_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int tenantId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonProperty(value = "creating_timestamp", access = JsonProperty.Access.READ_ONLY)
    private String creatingTimestamp;

    @Setter
    @JsonIgnore
    private String correlationId;

    @JsonProperty(value = "reviews", access = JsonProperty.Access.READ_ONLY)
    private Set<ReviewDto> reviewsSet;

    @JsonProperty(value = "images", access = JsonProperty.Access.READ_ONLY)
    private Set<ImageUrlDto> imageUrlSet;

    @JsonProperty(value = "categories", access = JsonProperty.Access.READ_ONLY)
    private Set<CategoryDto> categoriesSet;


    @Override
    @JsonIgnore
    public ProductDao toDao() {
        return new ProductEntity(
                id,
                tenantId,
                name,
                description,
                correlationId
        );
    }

    @Override
    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    @JsonIgnore
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
