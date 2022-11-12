package com.shop4me.auctiondatastream.domain.port.dao;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface AuctionDao {

    UUID getId();

    String getType();

    String getAuctionName();

    long getProductId();

    ZonedDateTime getPostingTime();

    ZonedDateTime getStartingTime();

    ZonedDateTime getEndingTime();
}
