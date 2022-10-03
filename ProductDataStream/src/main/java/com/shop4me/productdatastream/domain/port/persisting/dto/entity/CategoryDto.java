package com.shop4me.productdatastream.domain.port.persisting.dto.entity;

import com.shop4me.productdatastream.domain.port.persisting.dao.CategoryDao;

public interface CategoryDto {

    Long getId();

    String getName();

    String getPath();

    CategoryDao toDao();

    String getAbsolutePath();

    void setDefaultName();

    void setDefaultPath();
}
