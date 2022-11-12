package com.shop4me.productdatastream.domain.model.dao.productdatastorage;

import com.shop4me.productdatastream.domain.model.dto.Product;
import com.shop4me.productdatastream.domain.port.objects.dao.ProductDao;
import com.shop4me.productdatastream.domain.port.objects.dto.CategoryDto;
import com.shop4me.productdatastream.domain.port.objects.dto.ImageUrlDto;
import com.shop4me.productdatastream.domain.port.objects.dto.ProductDto;
import com.shop4me.productdatastream.domain.port.objects.dto.ReviewDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor

@SequenceGenerator(name="product_id_generator",sequenceName="PRODUCT_ID_GEN", allocationSize = 1)

@Entity

@DynamicInsert
@Table(name = "PRODUCT")
public class ProductEntity implements ProductDao {

    @Id
    @Column(name ="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
    private Long id;


    @Column(name = "TENANT", nullable = false)
    private int tenantId;

    @ColumnDefault(value = "'new product'")
    @Column(name = "NAME", length = 60)
    private String name;

    @Type(type = "text")
    @Column(name = "DESCRIPTION")
    private String description;

    @Getter(value = AccessLevel.PRIVATE)
    @ColumnDefault("CURRENT_TIMESTAMP()")
    @Column(name = "CREATING_TIME", updatable = false)
    private Timestamp creatingTimestamp;

    @OneToMany(mappedBy = "productId", fetch = FetchType.EAGER)
    private Set<ReviewEntity> reviewsSet;

    @Setter
    @OneToMany(mappedBy = "productId", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ImageUrlEntity> imageUrlSet;

    @Setter
    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "PRODUCT_IMPLEMENTING_CATEGORIES",
            joinColumns = { @JoinColumn(name = "PRODUCT") },
            inverseJoinColumns = { @JoinColumn(name = "CATEGORY") }
    )
    private Set<CategoryEntity> categoriesSet;

    @Override
    public ProductDto toDto() {
        return new Product(
                id,
                tenantId,
                name,
                description,
                creatingTimestampString(creatingTimestamp),
                reviewDtoSet(),
                imageUrlDtoSet(),
                categoryDtoSet()
        );
    }

    @Override
    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    private Set<ReviewDto> reviewDtoSet(){
        if(reviewsSet != null){
            return reviewsSet.stream()
                    .map(ReviewEntity::toDto)
                    .collect(Collectors.toSet());
        }else {
            return null;
        }
    }

    private Set<ImageUrlDto> imageUrlDtoSet(){
        if(imageUrlSet != null){
            return imageUrlSet.stream()
                    .map(ImageUrlEntity::toDto)
                    .collect(Collectors.toSet());
        }else {
            return null;
        }
    }

    private Set<CategoryDto> categoryDtoSet(){
        if(categoriesSet != null){
            return categoriesSet.stream()
                    .map(CategoryEntity::toDto)
                    .collect(Collectors.toSet());
        }else {
            return null;
        }
    }

    private String creatingTimestampString(Timestamp timestamp){
        if(timestamp != null){
            timestamp.setNanos(0);
            return timestamp.toString();
        }else {
            return null;
        }
    }

    public ProductEntity(Long id, int tenantId, String name, String description) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.description = description;
    }
}
