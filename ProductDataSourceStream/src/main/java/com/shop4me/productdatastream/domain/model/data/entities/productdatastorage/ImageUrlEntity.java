package com.shop4me.productdatastream.domain.model.data.entities.productdatastorage;

import com.shop4me.productdatastream.domain.model.data.dto.ImageUrl;
import com.shop4me.productdatastream.domain.port.persisting.dao.ImageUrlDao;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ImageUrlDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@NoArgsConstructor

@Entity

@DynamicInsert
@Table(name = "media_data_storage")
public class ImageUrlEntity implements ImageUrlDao {

    @Id
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();

    @Column(name ="product_id", nullable = false)
    private long productId;

    @Type(type = "text")
    @Column(name = "media_source_url", nullable = false)
    private String url;

    @ColumnDefault("1")
    @Column(name = "visibility")
    private Boolean visibility;

    @Getter(value = AccessLevel.PRIVATE)
    @ColumnDefault(value = "CURRENT_TIMESTAMP()")
    @Column(name = "creating_timestamp", nullable = false, updatable = false)
    private Timestamp creatingTimestamp;

    public ImageUrlEntity(String id, long productId, String url, Boolean visibility) {
        this.id = id;
        this.productId = productId;
        this.url = url;
        this.visibility = visibility;
    }

    @Override
    public ImageUrlDto toDto() {
        return new ImageUrl(id, productId, url, visibility);
    }

    @Override
    public String toString() {
        return "ImageUrlEntity{" +
                "id='" + id + '\'' +
                ", productId=" + productId +
                ", url='" + url + '\'' +
                ", visibility=" + visibility +
                ", creatingTimestamp=" + creatingTimestamp +
                '}';
    }
}
