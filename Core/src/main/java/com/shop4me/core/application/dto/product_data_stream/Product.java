package com.shop4me.core.application.dto.product_data_stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ImageUrlDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class Product implements ProductDto {

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
    private List<Review> reviews;

    @JsonProperty("images")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ImageUrl> imageUrls;

    @JsonProperty("categories")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Category> categories;

    public static Product fromDto(ProductDto productDto){
        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getDescription(),
                convertReviewDtoList(productDto.getReviews()),
                convertImageUrlDtoList(productDto.getImageUrls()),
                convertCategoryDtoList(productDto.getCategories())
        );
    }

    private static List<Review> convertReviewDtoList(List<ReviewDto> reviewDtoList){
        if(reviewDtoList != null){
            return reviewDtoList.stream()
                    .map(Review::fromDto)
                    .toList();
        }
        else { return null;}
    }

    private static List<ImageUrl> convertImageUrlDtoList(List<ImageUrlDto> imageUrlDtoList){
        if(imageUrlDtoList != null){
            return imageUrlDtoList.stream()
                    .map(ImageUrl::fromDto)
                    .toList();
        }
        else { return null;}
    }

    private static List<Category> convertCategoryDtoList(List<CategoryDto> categoryDtoList){
        if(categoryDtoList != null){
            return categoryDtoList.stream()
                    .map(Category::fromDto)
                    .toList();
        }
        else { return null;}
    }

    @Override
    public List<ReviewDto> getReviews() {
        if(reviews != null){
            return reviews.stream()
                    .map(ReviewDto.class::cast)
                    .toList();
        }
        else { return null;}
    }

    @Override
    public List<ImageUrlDto> getImageUrls() {
        if(imageUrls != null){
            return imageUrls.stream()
                    .map(ImageUrlDto.class::cast)
                    .toList();
        }
        else {return null;}
    }

    @Override
    public List<CategoryDto> getCategories() {
        if(categories != null){
            return categories.stream()
                    .map(CategoryDto.class::cast)
                    .toList();
        }
        else {return null;}
    }
}
