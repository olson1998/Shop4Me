package com.shop4me.core.application.dto.product_data_stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.productdatastream.ImageUrlDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor
@AllArgsConstructor

public class ImageUrl implements ImageUrlDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("media_source_url")
    private String url;

    @JsonProperty("visibility")
    private boolean visibility;

    public static ImageUrl fromDto(ImageUrlDto imageUrlDto){
        return new ImageUrl(
                imageUrlDto.getId(),
                imageUrlDto.getUrl(),
                imageUrlDto.getVisibility()
        );
    }

    @Override
    public boolean getVisibility() {
        return visibility;
    }

    @Override
    public String toString() {
        return "ImageUrl{" +
                "url='" + url + '\'' +
                ", visibility=" + visibility +
                '}';
    }
}
