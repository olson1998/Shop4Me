package com.shop4me.productdatastream.domain.model.request.product.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.productdatastream.domain.model.request.enumset.Operator;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.product.ProductProperty;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductSearchFilterDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode

@NoArgsConstructor
@AllArgsConstructor

public class ProductSearchFilter implements ProductSearchFilterDto {

    @JsonProperty("property")
    private ProductProperty property;

    @JsonProperty("operator")
    private Operator operator;

    @JsonProperty("value")
    private String value;

    @Override
    public String getProperty() {
        return property.name();
    }

    @Override
    public String getOperator() {
        return operator.name();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String where(){
        switch (property){
            case NAME -> {
                if(operator.equals(Operator.LIKE)){
                    return "p.name like '%" + value + "%' ";
                }
                else if (operator.equals(Operator.NOT)){
                    return "p.name not like '%" + value + "%' ";
                }
            }
            case CATEGORY -> {
                if(operator.equals(Operator.LIKE)){
                    return "concat(c.path, '.', '\"', c.name, '\"') like '" + value + "%' ";
                }
                else if (operator.equals(Operator.NOT)){
                    return "concat(c.path, '.', '\"', c.name, '\"') not like '" + value + "%' ";
                }
            }
            case DESCRIPTION -> {
                if(operator.equals(Operator.LIKE)){
                    return "p.description like '%" + value + "%' ";
                }
                else if (operator.equals(Operator.NOT)){
                    return "p.description not like '%" + value + "%' ";
                }
            }
        }
        return "";
    }

}
