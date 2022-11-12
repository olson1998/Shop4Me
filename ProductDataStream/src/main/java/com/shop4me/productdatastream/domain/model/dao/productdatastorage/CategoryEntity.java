package com.shop4me.productdatastream.domain.model.dao.productdatastorage;

import com.shop4me.productdatastream.domain.model.dto.Category;
import com.shop4me.productdatastream.domain.port.objects.dao.CategoryDao;
import com.shop4me.productdatastream.domain.port.objects.dto.CategoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor

@SequenceGenerator(name="category_id_generator",sequenceName="CATEGORY_ID_GEN", allocationSize = 1)

@Entity

@DynamicInsert
@Table(name = "CATEGORIES")
public class CategoryEntity implements CategoryDao {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_generator")
    private Long id;

    @Column(name = "TENANT", nullable = false)
    private int tenantId;

    @ColumnDefault(value = "'\"all\"'")
    @Column(name = "PATH", columnDefinition = "VARCHAR(255)")
    private String path;

    @ColumnDefault(value = "'new category'")
    @Column(name = "NAME", length = 32)
    private String name;

    @Column(name = "CREATING_TIME", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP()")
    private Timestamp creatingTimeStamp;

    public CategoryEntity(Long id, int tenantId, String path, String name) {
        this.id = id;
        this.tenantId = tenantId;
        this.path = path;
        this.name = name;
    }

    public CategoryEntity(long id) {
        this.id = id;
    }

    @Override
    public CategoryDto toDto() {
        return new Category(id, tenantId, path, name);
    }

    @Override
    public String toString() {
        return path + ".\"" + name + "\"";
    }
}
