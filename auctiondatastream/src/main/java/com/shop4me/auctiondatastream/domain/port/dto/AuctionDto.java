package com.shop4me.auctiondatastream.domain.port.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public interface AuctionDto {

    UUID getId();

    String getType();

    String getAuctionName();

    long getProductId();

    LocalDateTime getStartingTime();

    LocalDateTime getEndingTime();

    ZoneId getZoneId();
}
