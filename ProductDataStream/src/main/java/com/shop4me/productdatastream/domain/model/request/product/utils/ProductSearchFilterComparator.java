package com.shop4me.productdatastream.domain.model.request.product.utils;

import com.shop4me.productdatastream.domain.model.dao.productdatastorage.properties.ProductProperty;
import com.shop4me.productdatastream.domain.model.request.enumset.Operator;
import lombok.NonNull;

import java.util.Comparator;

public class ProductSearchFilterComparator implements Comparator<ProductSearchFilter> {

    @Override
    public int compare(ProductSearchFilter o1, ProductSearchFilter o2) {
        return Integer.compare(getPoints(o1), getPoints(o2));
    }

    private int getPoints(@NonNull ProductSearchFilter filter){
        switch (ProductProperty.valueOf(filter.getProperty())){
            case NAME -> {
                switch (Operator.valueOf(filter.getOperator())){
                    case LIKE -> {
                        return 0;
                    }
                    case NOT->{
                        return 5;
                    }
                }
            }
            case CATEGORY -> {
                switch (Operator.valueOf(filter.getOperator())){
                    case LIKE -> {
                        return 10;
                    }
                    case NOT->{
                        return 15;
                    }
                }
            }
            case DESCRIPTION -> {
                switch (Operator.valueOf(filter.getOperator())){
                    case LIKE -> {
                        return 20;
                    }
                    case NOT->{
                        return 25;
                    }
                }
            }
            case PHOTO_URL -> {
                return 1;
            }
        }
        return 0;
    }
}
