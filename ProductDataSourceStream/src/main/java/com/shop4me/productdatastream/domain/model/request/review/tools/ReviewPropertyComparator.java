package com.shop4me.productdatastream.domain.model.request.review.tools;

import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.review.ReviewProperty;
import lombok.NonNull;

import java.util.Comparator;

public class ReviewPropertyComparator implements Comparator<ReviewProperty> {
    @Override
    public int compare(ReviewProperty o1, ReviewProperty o2) {
        return Integer.compare(getPoints(o1), getPoints(o2));
    }

    private int getPoints(@NonNull ReviewProperty reviewProperty){
        switch (reviewProperty){
            case REVIEWER_NAME -> {
                return 0;
            }
            case TEXT -> {
                return 5;
            }
        }
        return 10;
    }
}
