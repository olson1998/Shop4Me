package com.shop4me.productdatastream.domain.model.request.product.tools;

import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.product.ProductProperty;

import java.util.Comparator;

public class ProductPropertyComparator implements Comparator<ProductProperty> {

    @Override
    public int compare(ProductProperty o1, ProductProperty o2) {
        return Integer.compare(getPoints(o1), getPoints(o2));
    }

    private int getPoints(ProductProperty property){
        switch (property){
            case NAME -> {
                return 0;
            }
            case CATEGORY -> {
                return 5;
            }
            case DESCRIPTION -> {
                return 10;
            }
            case PHOTO_URL -> {
                return 15;
            }
        }
        return 20;
    }
}
