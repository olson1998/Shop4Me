package com.shop4me.productdatastream.domain.model.data.entities.productdatastorage;

import com.shop4me.productdatastream.domain.model.data.dto.Product;
import com.shop4me.productdatastream.domain.port.persisting.dao.ProductDao;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ImageUrlDto;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductDto;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

@SequenceGenerator(name="product_id_generator",sequenceName="product_id_sequence", allocationSize = 1)

@Entity

@DynamicInsert
@Table(name = "product_data")
public class ProductEntity implements ProductDao {

    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
    private Long id;

    @ColumnDefault(value = "'new product'")
    @Column(name = "product_name", length = 60)
    private String name;

    @Type(type = "text")
    @Column(name = "description")
    private String description;

    @ColumnDefault("CURRENT_TIMESTAMP()")
    @Column(name = "creating_timestamp", updatable = false)
    private Timestamp creatingTimestamp;

    @OneToMany(mappedBy = "productId", fetch = FetchType.EAGER)
    private Set<ReviewEntity> reviewsSet;

    @Setter
    @OneToMany(mappedBy = "productId", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ImageUrlEntity> imageUrlSet;

    @Setter
    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_categories_map",
            joinColumns = { @JoinColumn(name = "product_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    private Set<CategoryEntity> categoriesSet;

    @Override
    public ProductDto toDto() {
        return new Product(
                id,
                name,
                description,
                creatingTimestamp.toString(),
                reviewDtoSet(),
                imageUrlDtoSet(),
                categoryDtoSet()
        );
    }

    private Set<ReviewDto> reviewDtoSet(){
        return reviewsSet.stream()
                .map(ReviewEntity::toDto)
                .collect(Collectors.toSet());
    }

    private Set<ImageUrlDto> imageUrlDtoSet(){
        return imageUrlSet.stream()
                .map(ImageUrlEntity::toDto)
                .collect(Collectors.toSet());
    }

    private Set<CategoryDto> categoryDtoSet(){
        return categoriesSet.stream()
                .map(CategoryEntity::toDto)
                .collect(Collectors.toSet());
    }

    public ProductEntity(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public ProductEntity(Long id, String name, String description, Timestamp creatingTimestamp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatingTimestamp = creatingTimestamp;
    }
}
