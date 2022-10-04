package com.shop4me.core.application.dto.product_data_stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter

@AllArgsConstructor
@NoArgsConstructor

public class Review implements ReviewDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonProperty("product_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long productId;

    @JsonProperty("reviewer_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long reviewerId;

    @JsonProperty("reviewer_name")
    private String reviewerName;

    @JsonProperty("review_points")
    private byte points;

    @JsonProperty("text")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String text;


    @JsonProperty(value = "publishing_timestamp", access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String publishingDate;

    public static Review fromDto(@NonNull ReviewDto reviewDto){
        return new Review(
                reviewDto.getId(),
                reviewDto.getProductId(),
                reviewDto.getReviewerId(),
                reviewDto.getReviewerName(),
                reviewDto.getPoints(),
                reviewDto.getText(),
                reviewDto.getPublishingDate()
        );
    }

    @Override
    public String savingKey() {
        return "Review{" +
                "productId=" + productId +
                ", reviewerId=" + reviewerId +
                ", points=" + points +
                '}';
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", productId=" + productId +
                ", reviewerId=" + reviewerId +
                ", reviewerName='" + reviewerName + '\'' +
                '}';
    }
}
