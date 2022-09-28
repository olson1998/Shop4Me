package com.shop4me.productdatastream.domain.model.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.port.persisting.dao.CategoryDao;
import com.shop4me.productdatastream.domain.port.persisting.dao.ImageUrlDao;
import com.shop4me.productdatastream.domain.port.persisting.dao.ProductDao;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ImageUrlDto;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductDto;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter

@AllArgsConstructor
@NoArgsConstructor

public class Product implements ProductDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonProperty(value = "creating_timestamp", access = JsonProperty.Access.READ_ONLY)
    private String creatingTimestamp;

    @JsonProperty(value = "reviews", access = JsonProperty.Access.READ_ONLY)
    private Set<ReviewDto> reviewsSet;

    @JsonProperty(value = "images", access = JsonProperty.Access.READ_ONLY)
    private Set<ImageUrlDto> imageUrlSet;

    @JsonProperty(value = "categories", access = JsonProperty.Access.READ_ONLY)
    private Set<CategoryDto> categoriesSet;


    @Override
    public ProductDao toDao() {
        return new ProductEntity(
                id,
                name,
                description
        );
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
