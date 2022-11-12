package com.shop4me.productdatastream.domain.model.dao.productdatastorage;

import com.shop4me.productdatastream.domain.model.dto.ImageUrl;
import com.shop4me.productdatastream.domain.port.objects.dao.ImageUrlDao;
import com.shop4me.productdatastream.domain.port.objects.dto.ImageUrlDto;
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
@Table(name = "PRODUCT$MEDIA_STORAGE")
public class ImageUrlEntity implements ImageUrlDao {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name ="PROD", nullable = false)
    private long productId;

    @Type(type = "text")
    @Column(name = "MEDIA_SOURCE_URL", nullable = false)
    private String url;

    @ColumnDefault("1")
    @Column(name = "VISIBILITY")
    private Boolean visibility;

    @Getter(value = AccessLevel.PRIVATE)
    @ColumnDefault(value = "CURRENT_TIMESTAMP()")
    @Column(name = "CREATING_TIME", nullable = false, updatable = false)
    private Timestamp creatingTimestamp;

    public ImageUrlEntity(String id, long productId, String url, Boolean visibility) {
        this.id = id;
        this.productId = productId;
        this.url = url;
        this.visibility = visibility;
    }

    public ImageUrlEntity(String id) {
        this.id = id;
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
