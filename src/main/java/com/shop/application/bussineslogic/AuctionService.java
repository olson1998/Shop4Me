package com.shop.application.bussineslogic;

import com.shop.application.repositories.auctionsdbdao.AuctionsRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor

@Service
public class AuctionService {

    private final AuctionsRepo repository;
}
