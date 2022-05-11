package com.shop.application.entities.auctionsdb;

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
    private int auction_id;

    @Column(name = "product_id", nullable = false)
    private int product_id;

    @Column(name = "buyer_id")
    private Long customerID;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price", nullable = false, columnDefinition = "default 0")
    private double price;

    @Column(name = "start_date", nullable = false)
    private Timestamp start_date;

    @Column(name = "finish_date")
    private Timestamp finish_date;

    /*@OneToOne
    private Product product;*/

}
