package com.shop4me.productdatastream.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.application.configuration.H2ProductDataSourceConfig;
import com.shop4me.productdatastream.application.configuration.H2Shop4MeSecurityCredentialsDataSourceConfig;
import com.shop4me.productdatastream.domain.model.data.dto.Category;
import com.shop4me.productdatastream.domain.model.data.dto.Product;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
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

import static com.shop4me.productdatastream.application.request.ProductOperationTestRequest.productObtainCoreRequest;
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
class ProductObtainRequestMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaContext jpaContext;

    @Autowired
    private ObjectMapper mapper;

    private static final String TARGET_PRODUCT_NAME = "target-product";

    private static final Product[] TEST_PRODUCT_ARRAY = {
            new Product("test-product-1"),
            new Product("test-product-2"),
            new Product("test-product-3"),
            new Product("test-product-4"),
            new Product(TARGET_PRODUCT_NAME),
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

    }

    @AfterEach
    public void deleteTestData(){

    }

    @Test
    void shouldRespondWith200AndBodyContainingListOfProductsWithIdProvidedInArgs() throws Exception {
        var targetProductId = selectTargetProductId();
        long[] payload = {targetProductId};

        var productObtainRequest = productObtainCoreRequest(payload);
        var productObtainRequestJson = mapper.writeValueAsString(productObtainRequest);

        var mockMvcResult = this.mockMvc.perform(post("/rq")
                .content(productObtainRequestJson)
                .contentType(APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mockMvcResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(print())
                .andReturn();
    }

    private long selectTargetProductId(){
        return jpaContext.getEntityManagerByManagedType(ProductEntity.class)
                .createQuery("select p.id from ProductEntity p where p.name= :name", Long.class)
                .setParameter("name", TARGET_PRODUCT_NAME)
                .getSingleResult();
    }
}
