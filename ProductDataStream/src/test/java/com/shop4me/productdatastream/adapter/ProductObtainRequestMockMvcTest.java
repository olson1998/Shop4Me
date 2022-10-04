package com.shop4me.productdatastream.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.adapter.utils.ExampleEntitiesInsertInterface;
import com.shop4me.productdatastream.application.configuration.H2ProductDataSourceConfig;
import com.shop4me.productdatastream.application.configuration.H2Shop4MeSecurityCredentialsDataSourceConfig;
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

import java.util.List;

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
class ProductObtainRequestMockMvcTest implements ExampleEntitiesInsertInterface {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaContext jpaContext;

    @Autowired
    private ObjectMapper mapper;

    private static final String TARGET_PRODUCT_NAME = "test-product-1";

    private static final Product[] TEST_PRODUCT_ARRAY = {
            new Product(TARGET_PRODUCT_NAME),
            new Product("test-product-2"),
            new Product("test-product-3"),
            new Product("test-product-4"),
            new Product("test-product-5"),
            new Product("test-product-6")
    };

    @BeforeEach
    public void insertTestData(){
        insertTestProducts(TEST_PRODUCT_ARRAY);
    }

    @AfterEach
    public void deleteTestData(){
        cleanTestDatabase();
    }

    @Test
    void shouldRespondWith200AndBodyContainingListOfProductsWithIdProvidedInArgs() throws Exception {
        var requestTargetProductId = getTargetProductId();

        var productObtainRequest = productObtainCoreRequest(requestTargetProductId);
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
                .andExpect(jsonPath("$.[0].name").value(TARGET_PRODUCT_NAME))
                .andExpect(jsonPath("$[0].id").value(requestTargetProductId));
    }

    @Test
    void shouldRespondWith400AndBodyOfExceptionMapIfRequestContainingEmptyProductIdArray() throws Exception {
        var productObtainRequest = productObtainCoreRequest();
        var productObtainRequestJson = mapper.writeValueAsString(productObtainRequest);

        var expectedJson =
                "{\"exception\":\"Could not execute request: PRODUCT OBTAIN, reason: [com.shop4me.productdatastream.domain.model.exception.EmptyPayloadException: Empty payload]\"}";

        var mockMvcResult = this.mockMvc.perform(post("/rq")
                        .content(productObtainRequestJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mockMvcResult))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(expectedJson))
                .andDo(print());
    }

    private long getTargetProductId(){
        return jpaContext.getEntityManagerByManagedType(ProductEntity.class)
                .createQuery("select p.id from ProductEntity p where p.name= :name", Long.class)
                .setParameter("name",TARGET_PRODUCT_NAME)
                .getSingleResult();
    }

    private String writeSelectedJpql(List<Long> productsIdList){
        var jpql = new StringBuilder("select p from ProductEntity p ");
        if(productsIdList.size() > 0){
            var idIterator = productsIdList.listIterator();
            jpql.append("where ");
            while (idIterator.hasNext()){
                jpql.append("p.id=").append(idIterator.next()).append(" ");
                if(idIterator.hasNext()){
                    jpql.append("or ");
                }
            }
        }
        return jpql.toString();
    }

    @Override
    public JpaContext springData() {
        return jpaContext;
    }

    @Override
    public Logger log() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
