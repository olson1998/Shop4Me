package com.shop.application.repositories;

import com.shop.application.entities.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionsRepo extends JpaRepository<Auction, Long> {
}
