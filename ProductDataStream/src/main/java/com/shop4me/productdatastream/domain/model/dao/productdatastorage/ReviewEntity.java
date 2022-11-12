package com.shop4me.productdatastream.domain.model.dao.productdatastorage;

import com.shop4me.productdatastream.domain.model.dto.Review;
import com.shop4me.productdatastream.domain.port.objects.dao.ReviewDao;
import com.shop4me.productdatastream.domain.port.objects.dto.ReviewDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter

@NoArgsConstructor

@SequenceGenerator(name="review_id_generator",sequenceName="REV_ID_GEN", allocationSize = 1)

@Entity

@DynamicInsert
@Table(name = "PRODUCT$USERS_REVIEWS")
public class ReviewEntity implements ReviewDao {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_id_generator")
    private Long id;

    @Column(name = "PROD", nullable = false, updatable = false)
    private long productId;

    @Column(name = "REV_ID", nullable = false, updatable = false)
    private long reviewerId;

    @ColumnDefault(value = "'Anonymous'")
    @Column(name ="REV_NAME", length = 30)
    private String reviewerName;

    @Column(name ="POINTS", columnDefinition = "INT DEFAULT 10", updatable = false, nullable = false)
    private byte points;

    @Type(type = "text")
    @Column(name = "TEXT")
    private String text;

    @Getter(value = AccessLevel.PRIVATE)
    @Column(name ="PUBLISH_TIME", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP()")
    private Timestamp publishingTimestamp;

    public ReviewEntity(Long id, Long productId, Long reviewerId, String reviewerName, byte points, String text) {
        this.id = id;
        this.productId = productId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.points = points;
        this.text = text;
    }


    @Override
    public ReviewDto toDto() {
        return new Review(
                id,
                productId,
                reviewerId,
                reviewerName,
                points,
                text,
                publishingTimestamp.toString()
        );
    }

    @Override
    public String toString() {
        return "ReviewEntity{" +
                "id=" + id +
                ", productId=" + productId +
                ", reviewerId=" + reviewerId +
                ", reviewerName='" + reviewerName + '\'' +
                ", points=" + points +
                ", text='" + text + '\'' +
                ", publishingTimestamp=" + publishingTimestamp +
                '}';
    }
}
