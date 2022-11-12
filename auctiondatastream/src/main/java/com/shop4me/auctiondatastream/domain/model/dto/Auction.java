package com.shop4me.auctiondatastream.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.auctiondatastream.domain.port.dao.AuctionDao;
import com.shop4me.auctiondatastream.domain.port.dto.AuctionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter

@AllArgsConstructor
@NoArgsConstructor
public class Auction implements AuctionDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("auction_name")
    private String auctionName;

    @JsonProperty("product_id")
    private long productId;

    @JsonProperty(value = "publishing_time", access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime publishingTime;

    @JsonProperty("starting_time")
    private LocalDateTime startingTime;

    @JsonProperty("ending_time")
    private LocalDateTime endingTime;

    private final ZoneId zoneId = ZoneId.of("Europe/Warsaw");

    public static AuctionDto fromDao(@NonNull AuctionDao auctionDao){
        return new Auction(
                auctionDao.getId(),
                auctionDao.getType(),
                auctionDao.getAuctionName(),
                auctionDao.getProductId(),
                auctionDao.getPostingTime().toLocalDateTime(),
                auctionDao.getStartingTime().toLocalDateTime(),
                auctionDao.getEndingTime().toLocalDateTime()
        );
    }
}
