package com.shop4me.productdatastream.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.adapter.utils.ProductsCategoriesLinkInsert;
import com.shop4me.productdatastream.application.configuration.H2ProductDataSourceConfig;
import com.shop4me.productdatastream.application.configuration.H2Shop4MeSecurityCredentialsDataSourceConfig;
import com.shop4me.productdatastream.domain.model.data.dto.Category;
import com.shop4me.productdatastream.domain.model.data.dto.Product;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.properties.product.ProductProperty;
import com.shop4me.productdatastream.domain.model.request.enumset.Operator;
import com.shop4me.productdatastream.domain.model.request.product.ProductSearchRequestImpl;
import com.shop4me.productdatastream.domain.model.request.product.tools.ProductSearchFilter;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductSearchFilterDto;
import com.shop4me.productdatastream.domain.port.requesting.CoreRequest;
import com.shop4me.productdatastream.domain.port.requesting.ProductSearchRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shop4me.productdatastream.application.request.ProductOperationTestRequest.PRODUCT;
import static com.shop4me.productdatastream.application.request.ProductOperationTestRequest.productSearchCoreRequest;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {
        H2Shop4MeSecurityCredentialsDataSourceConfig.class,
        H2ProductDataSourceConfig.class
})

@Transactional("productDataStreamTransactionManager")

@AutoConfigureMockMvc(addFilters = false)

@SpringBootTest
class ProductSearchRequestMockMvcTest implements ProductsCategoriesLinkInsert {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaContext jpaContext;

    @Autowired
    private ObjectMapper mapper;

    private final LinkedMultiValueMap<String, String> productImplementingCategoriesMap =
            new LinkedMultiValueMap<>();

    private static final String SEARCHED_PRODUCT_NAME = "test-product";

    private static final List<String> SEARCH_PRODUCT_IN_THOSE_CATEGORIES = List.of(
            "\"all\".\"test-category-1\"",
            "\"all\".\"test-category-3\""
    );

    private static final Product[] TEST_PRODUCT_ARRAY = {
            new Product("test-product-1"),
            new Product("test-product-2"),
            new Product("test-product-3"),
            new Product("test-product-4"),
            new Product("test-product-5"),
            new Product("test-product-6")
    };

    private static final Category[] TEST_CATEGORY_ARRAY = {
            new Category(null, "\"all\"", "test-category-1"),
            new Category(null, "\"all\"", "test-category-2"),
            new Category(null, "\"all\"", "test-category-3"),
            new Category(null, "\"all\"", "test-category-4")
    };

    @BeforeEach
    public void insertTestData(){
        insertTestProducts(TEST_PRODUCT_ARRAY);
        insertTestCategories(TEST_CATEGORY_ARRAY);

        linkProductWithCategories(
                TEST_PRODUCT_ARRAY[0].getName(),
                absolutePathList(TEST_CATEGORY_ARRAY[0], TEST_CATEGORY_ARRAY[3])
        );
        linkProductWithCategories(
                TEST_PRODUCT_ARRAY[1].getName(),
                absolutePathList(TEST_CATEGORY_ARRAY[1], TEST_CATEGORY_ARRAY[2], TEST_CATEGORY_ARRAY[3])
        );
        linkProductWithCategories(
                TEST_PRODUCT_ARRAY[2].getName(),
                absolutePathList(TEST_CATEGORY_ARRAY[2])
        );
        linkProductWithCategories(
                TEST_PRODUCT_ARRAY[3].getName(),
                absolutePathList(TEST_CATEGORY_ARRAY[0], TEST_CATEGORY_ARRAY[3], TEST_CATEGORY_ARRAY[2])
        );
        linkProductWithCategories(
                TEST_PRODUCT_ARRAY[4].getName(),
                absolutePathList(TEST_CATEGORY_ARRAY[1])
        );
        createProductImplementingCategoriesWeb();
    }

    @AfterEach
    public void deleteTestData(){
        cleanAllLinks();
        cleanTestDatabase();
    }

    @Test
    void shouldRespondWith200AndBodyContainingListOfProductsWithIdProvidedInArgs() throws Exception {
        var searchFilters = createProductSearchFilterList();
        var productSearchCoreRequest = productSearchCoreRequest(searchFilters);
        var productSearchCoreRequestJson = mapper.writeValueAsString(productSearchCoreRequest);

        var mockMvcResult = this.mockMvc.perform(post("/rq")
                        .content(productSearchCoreRequestJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mockMvcResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void shouldRespondWith400AndMapOfExceptionIfRequestContainsArrayWithNoSearchFilters() throws Exception {
        var productSearchCoreRequest = productSearchCoreRequest(new ProductSearchFilterDto[0]);
        var productSearchCoreRequestJson = mapper.writeValueAsString(productSearchCoreRequest);

        var expectedJson =
                "{\"exception\":\"Could not execute request: PRODUCT SEARCH, reason: [com.shop4me.productdatastream.domain.model.exception.EmptyPayloadException: Empty payload]\"}";

        var mockMvcResult = this.mockMvc.perform(post("/rq")
                        .content(productSearchCoreRequestJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mockMvcResult))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    private ProductSearchFilter[] createProductSearchFilterList(){
        var searchFilters = new ArrayList<ProductSearchFilter>();

        searchFilters.add(new ProductSearchFilter(ProductProperty.NAME, Operator.LIKE, SEARCHED_PRODUCT_NAME));

        SEARCH_PRODUCT_IN_THOSE_CATEGORIES.forEach(category ->{
            var filter = new ProductSearchFilter(ProductProperty.CATEGORY, Operator.LIKE, category);
            searchFilters.add(filter);
        });

        var filtersQty = searchFilters.size();
        var filtersArray = new ProductSearchFilter[filtersQty];

        for(int i = 0; i< filtersQty; i++){
            filtersArray[i] = searchFilters.get(i);
        }
        return filtersArray;
    }

    @Override
    public JpaContext springData() {
        return jpaContext;
    }

    @Override
    public Logger log() {
        return LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public LinkedMultiValueMap<String, String> productsImplementingCategories() {
        return productImplementingCategoriesMap;
    }
}
