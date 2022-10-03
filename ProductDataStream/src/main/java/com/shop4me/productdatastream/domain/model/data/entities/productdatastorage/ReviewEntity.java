package com.shop4me.productdatastream.domain.model.data.entities.productdatastorage;

import com.shop4me.productdatastream.domain.model.data.dto.Review;
import com.shop4me.productdatastream.domain.port.persisting.dao.ReviewDao;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ReviewDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter

@NoArgsConstructor

@SequenceGenerator(name="review_id_generator",sequenceName="review_id_sequence", allocationSize = 1)

@Entity

@DynamicInsert
@Table(name = "user_reviews")
public class ReviewEntity implements ReviewDao {

    @Setter
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_id_generator")
    private Long id;

    @Column(name = "product_id", nullable = false, updatable = false)
    private long productId;

    @Column(name = "reviewing_user_id", nullable = false, updatable = false)
    private long reviewerId;

    @ColumnDefault(value = "'Anonymous'")
    @Column(name ="reviewer_name", length = 30)
    private String reviewerName;

    @Column(name ="review_points", columnDefinition = "INT DEFAULT 10", updatable = false, nullable = false)
    private byte points;

    @Type(type = "text")
    @Column(name = "review_text")
    private String text;

    @Column(name ="publishing_timestamp", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP()")
    private Timestamp publishingTimestamp;

    public ReviewEntity(Long productId, Long reviewerId, String reviewerName, byte points, String text) {
        this.productId = productId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.points = points;
        this.text = text;
    }

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
