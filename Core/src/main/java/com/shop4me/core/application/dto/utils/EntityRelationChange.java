package com.shop4me.core.application.dto.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.utils.RelationEdit;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityRelationChange implements RelationEdit {

    @JsonProperty(value = "operation", required = true)
    private EntityRelationOperation entityRelationOperation;

    @Getter
    @JsonProperty(value = "value", required = true)
    private String value;

    @Override
    public String getOperation() {
        return entityRelationOperation.name();
    }

    @Override
    public String toString() {
        return String.format("{%s : %s}", entityRelationOperation.name(), value);
    }
}
