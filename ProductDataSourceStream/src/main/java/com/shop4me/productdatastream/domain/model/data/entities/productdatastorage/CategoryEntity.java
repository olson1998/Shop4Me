package com.shop4me.productdatastream.domain.model.data.entities.productdatastorage;

import com.shop4me.productdatastream.domain.model.data.dto.Category;
import com.shop4me.productdatastream.domain.port.persisting.dao.CategoryDao;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.CategoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor

@SequenceGenerator(name="category_id_generator",sequenceName="category_id_sequence", allocationSize = 1)

@Entity

@DynamicInsert
@Table(name = "categories")
public class CategoryEntity implements CategoryDao {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_generator")
    private Long id;

    @ColumnDefault(value = "'new category'")
    @Column(name = "category_name", length = 32)
    private String name;

    @ColumnDefault(value = "'\"all\"'")
    @Column(name = "category_path", columnDefinition = "VARCHAR(255)")
    private String path;

    @Column(name = "creating_timestamp", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP()")
    private Timestamp creatingTimeStamp;

    public CategoryEntity(Long id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    @Override
    public CategoryDto toDto() {
        return new Category(id, name, path);
    }

    @Override
    public String toString() {
        return path + ".\"" + name + "\"";
    }
}
