package com.shop.application.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "users_reviews")
public class Review {

    @Id
    @Column(name="review_id")
    private int review_id;

    @Column(name="product_id", nullable = false, unique = true ,columnDefinition = "INT(11) UNSIGNED")
    private int product_id;

    @Column(name="publishing_date", nullable = false)
    private Timestamp publishing_date;

    @Column(name="commentator_name", nullable = false)
    private String commentator_name;

    @Column(name="stars", nullable = false, columnDefinition = "TINYINT")
    private byte stars;

    @Column(name="text", columnDefinition = "text")
    private String text;
}
