package com.shop4me.core.model;

import com.shop4me.core.application.dto.productdatastream.Category;
import com.shop4me.core.application.dto.productdatastream.Product;

public class ConstantTestDto {

    public static final Category[] CATEGORIES_TO_SAVE = {
            new Category(null, "test-1", "\"all\""),
            new Category(null, "test-2", "\"all\"")
    };

    public static final Product[] PRODUCTS_TO_SAVE = {
            new Product(null,"test-product-1", null, null, null, null),
            new Product(null,"test-product-2", null, null, null, null)
    };

}
