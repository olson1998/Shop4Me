package com.shop4me.productdatastream.domain.port.objects.dto;

import com.shop4me.productdatastream.domain.port.objects.dao.CategoryDao;

public interface CategoryDto {

    Long getId();

    int getTenantId();

    String getName();

    String getPath();

    CategoryDao toDao();

    String getAbsolutePath();

    void setDefaultName();

    void setDefaultPath();

    void setTenantId(int tenantId);
}
