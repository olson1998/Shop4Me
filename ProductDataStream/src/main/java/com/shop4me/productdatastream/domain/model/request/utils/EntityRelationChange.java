package com.shop4me.productdatastream.domain.model.request.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EntityRelationChange {

    @JsonProperty(value = "operation", required = true)
    private EntityRelationOperation entityRelationOperation;

    @JsonProperty(value = "value", required = true)
    private String value;

    @Override
    public String toString() {
        return String.format(
                "{%s : %s}",
                entityRelationOperation.name(),
                value
        );
    }
}
