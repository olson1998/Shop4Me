package com.shop4me.auctiondatastream.domain.model.dao;

import com.shop4me.auctiondatastream.domain.port.dao.AuctionDao;
import com.shop4me.auctiondatastream.domain.port.dto.AuctionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter

@NoArgsConstructor
@AllArgsConstructor

@Document("Shop4Me$Auction_data")
public class AuctionEntity implements AuctionDao {

    @Id
    private UUID id;

    @Field("type")
    private AuctionType type;

    @Field("name")
    private String auctionName;

    @Field("product_id")
    private long productId;

    @Field("posting_time")
    private ZonedDateTime postingTime;

    @Field("starting_time")
    private ZonedDateTime startingTime;

    @Field("ending_time")
    private ZonedDateTime endingTime;

    @Override
    public String getType(){
        return type.name();
    }

    public static AuctionDao fromDto(@NonNull AuctionDto auctionDto){
        return new AuctionEntity(
                auctionDto.getId(),
                AuctionType.valueOf(auctionDto.getType()),
                auctionDto.getAuctionName(),
                auctionDto.getProductId(),
                ZonedDateTime.now(auctionDto.getZoneId()),
                ZonedDateTime.of(auctionDto.getStartingTime(), auctionDto.getZoneId()),
                ZonedDateTime.of(auctionDto.getEndingTime(), auctionDto.getZoneId())
        );
    }
}
