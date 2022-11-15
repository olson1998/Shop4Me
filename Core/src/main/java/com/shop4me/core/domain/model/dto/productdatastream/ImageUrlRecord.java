package com.shop4me.core.domain.model.dto.productdatastream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.productdatastream.ImageUrlDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageUrlRecord implements ImageUrlDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("media_source_url")
    private String url;

    @JsonProperty("visibility")
    private boolean visibility;

    @Override
    public boolean getVisibility() {
        return visibility;
    }
}
