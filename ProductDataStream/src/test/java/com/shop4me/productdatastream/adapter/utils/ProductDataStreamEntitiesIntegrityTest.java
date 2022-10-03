package com.shop4me.productdatastream.adapter.utils;

import com.shop4me.productdatastream.domain.model.data.dto.Category;
import com.shop4me.productdatastream.domain.model.data.dto.ImageUrl;
import com.shop4me.productdatastream.domain.model.data.dto.Product;
import com.shop4me.productdatastream.domain.model.data.dto.Review;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.CategoryEntity;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ImageUrlEntity;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ReviewEntity;
import org.slf4j.Logger;
import org.springframework.data.jpa.repository.JpaContext;

import java.util.Arrays;

public interface ProductDataStreamEntitiesIntegrityTest {

    JpaContext springData();

    Logger log();

    default void insertTestCategories(Category ... categories){
        Arrays.stream(categories).forEach(this::insertTestCategoriesIntoH2Db);

    }

    default void insertTestProducts(Product ... products){
        Arrays.stream(products).forEach(this::insertTestProductsIntoH2Db);
    }

    default void insertTestReviews(Review ... reviews){
        Arrays.stream(reviews).forEach(this::insertTestReviewsIntoH2Db);
    }

    default void insertTestImageUrls(ImageUrl ... imageUrls){
        Arrays.stream(imageUrls).forEach(this::insertImageUrlsIntoH2Db);
    }

    default void cleanTestDatabase(){
        var productEntityManager =
                springData().getEntityManagerByManagedType(ProductEntity.class);

        var categoryEntityManager =
                springData().getEntityManagerByManagedType(CategoryEntity.class);

        var reviewsEntityManager =
                springData().getEntityManagerByManagedType(ReviewEntity.class);

        var imageUrlsEntityManager =
                springData().getEntityManagerByManagedType(ImageUrlEntity.class);

        var deletedProductsQty = productEntityManager
                .createQuery("delete from ProductEntity p where p.id is not null")
                .executeUpdate();

        var deletedCategoriesQty = categoryEntityManager
                .createQuery("delete from CategoryEntity c where c.id is not null")
                .executeUpdate();

        var deletedReviewsQty = reviewsEntityManager
                .createQuery("delete from ReviewEntity r where r.id is not null")
                .executeUpdate();

        var deletedImageUrls = imageUrlsEntityManager
                .createQuery("delete from ImageUrlEntity i where i.id is not null")
                .executeUpdate();

        log().info("Deleted {} Products, {} Categories, {} Reviews, {} Image Urls",
                deletedProductsQty,
                deletedCategoriesQty,
                deletedReviewsQty,
                deletedImageUrls
        );
    }

    private void insertTestProductsIntoH2Db(Product ... products){
        Arrays.stream(products)
                .peek(c-> log().info("Persisting: {}", c.toString()))
                .map(Product::toDao)
                .forEach(springData().getEntityManagerByManagedType(ProductEntity.class)::persist);
    }

    private void insertTestCategoriesIntoH2Db(Category ... categories ){
        Arrays.stream(categories)
                .peek(p-> log().info("Persisting: {}", p.toString()))
                .map(Category::toDao)
                .forEach(springData().getEntityManagerByManagedType(CategoryEntity.class)::persist);
    }

    private void insertTestReviewsIntoH2Db(Review... reviews){
        Arrays.stream(reviews)
                .peek(r-> log().info("Persisting: {}", r.toString()))
                .map(Review::toDao)
                .forEach(springData().getEntityManagerByManagedType(ReviewEntity.class)::persist);
    }

    private void insertImageUrlsIntoH2Db(ImageUrl ... imageUrls){
        Arrays.stream(imageUrls)
                .peek(i-> log().info("Persisting: {}", i.toString()))
                .map(ImageUrl::toDao)
                .forEach(springData().getEntityManagerByManagedType(ImageUrlEntity.class)::persist);
    }
}
