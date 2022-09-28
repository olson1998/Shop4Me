package com.shop4me.productdatastream.domain.model.data;

public class ReviewEntityJpqlRepository {

    public static final String REVIEW_DELETE_JPQL =
            "delete from ReviewEntity r where r.id= 1 and r.reviewerId= 1 and r.productId= 1";
}
