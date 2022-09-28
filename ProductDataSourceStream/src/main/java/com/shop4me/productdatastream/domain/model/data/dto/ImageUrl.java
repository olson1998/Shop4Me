package com.shop4me.productdatastream.domain.model.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ImageUrlEntity;
import com.shop4me.productdatastream.domain.port.persisting.dao.ImageUrlDao;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ImageUrlDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@AllArgsConstructor
@NoArgsConstructor
public class ImageUrl implements ImageUrlDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty(value = "product_id", required = true)
    private long productId;

    @JsonProperty(value = "media_source_url", required = true)
    private String url;

    @JsonProperty("visibility")
    private Boolean visibility;

    @Override
    public ImageUrlDao toDao() {
        return new ImageUrlEntity(id, productId, url, visibility);
    }
}
