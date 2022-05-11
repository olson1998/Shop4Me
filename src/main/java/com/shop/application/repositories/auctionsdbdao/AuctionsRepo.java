package com.shop.application.repositories.auctionsdbdao;

import com.shop.application.entities.auctionsdb.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionsRepo extends JpaRepository<Auction, Long> {

}
