package com.shop4me.core.domain.model.dto.productdatastream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ImageUrlDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import java.util.Collections;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter

@NoArgsConstructor
public class ProductRecord implements ProductDto {

    @Getter
    @JsonProperty(value = "id")
    private Long id;

    @Getter
    @JsonProperty("name")
    private String name;

    @Getter
    @JsonProperty("description")
    private String description;

    @JsonProperty(value = "reviews", access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ReviewRecord> reviews;

    @JsonProperty("images")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ImageUrlRecord> imageUrls;

    @JsonProperty("categories")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CategoryRecord> categories;

    @Override
    public List<ReviewDto> getReviews() {
        if(reviews !=null){
            return reviews.stream()
                    .map(ReviewDto.class::cast)
                    .toList();
        }else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<ImageUrlDto> getImageUrls(){
        if(imageUrls !=null){
            return imageUrls.stream()
                    .map(ImageUrlDto.class::cast)
                    .toList();
        }else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CategoryDto> getCategories() {
        if(categories!=null){
            return categories.stream()
                    .map(CategoryDto.class::cast)
                    .toList();
        }else {
            return Collections.emptyList();
        }
    }
}
