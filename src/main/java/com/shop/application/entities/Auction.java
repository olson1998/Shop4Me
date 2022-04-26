package com.shop.application.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "auctions_data")
public class Auction {

    @Id
    @Column(name = "auction_id")
    private Long auction_id;

    @Column(name = "product_id", nullable = false)
    private Long product_id;

    @Column(name = "price", nullable = false, columnDefinition = "INT(11) UNSIGNED default 0")
    private double price;

    @Column(name = "start_date", nullable = false)
    private Timestamp start_date;

    @Column(name = "finish_date")
    private Timestamp finish_date;

    /*@OneToOne(mappedBy = "product_id", fetch = FetchType.EAGER)
    private Product product;*/

}
