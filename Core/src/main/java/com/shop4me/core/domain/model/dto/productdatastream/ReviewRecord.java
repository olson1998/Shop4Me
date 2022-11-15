package com.shop4me.core.domain.model.dto.productdatastream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRecord implements ReviewDto {

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
}
