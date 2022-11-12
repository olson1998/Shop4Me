package com.shop4me.productdatastream.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.ReviewEntity;
import com.shop4me.productdatastream.domain.port.objects.dao.ReviewDao;
import com.shop4me.productdatastream.domain.port.objects.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@AllArgsConstructor
@NoArgsConstructor

public class Review implements ReviewDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty(value = "product_id", required = true)
    private long productId;

    @JsonProperty(value = "reviewer_id", required = true)
    private long reviewerId;

    @JsonProperty("reviewer_name")
    private String reviewerName;

    @JsonProperty(value = "points", required = true)
    private byte points;

    @JsonProperty(value = "text")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String text;

    @JsonProperty(value = "publishing_timestamp", access = JsonProperty.Access.WRITE_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String publishingTimeStamp;

    @Override
    @JsonIgnore
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", productId=" + productId +
                ", reviewerId=" + reviewerId +
                '}';
    }

    @Override
    @JsonIgnore
    public ReviewDao toDao() {
        return new ReviewEntity(
                id,
                productId,
                reviewerId,
                reviewerName,
                points,
                text
        );
    }
}
