package com.shop.application.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name="product_id")
    private int product_id;

    @Column(name="product_name", nullable = false)
    private String product_name;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="photoURL", nullable = false)
    private String photoURL;

    @Column(name = "adding_date", nullable = false)
    private Timestamp addingDate;

    @OneToMany(mappedBy = "product_id", fetch = FetchType.EAGER)
    private List<Review> products_reviews;
}

