package com.shop4me.productdatastream.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.application.configuration.H2ProductDataSourceConfig;
import com.shop4me.productdatastream.application.configuration.H2Shop4MeSecurityCredentialsDataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@ContextConfiguration(classes = {
        H2Shop4MeSecurityCredentialsDataSourceConfig.class,
        H2ProductDataSourceConfig.class
})

@Transactional("productDbTransactionManager")

@AutoConfigureMockMvc(addFilters = false)

@SpringBootTest
class ProductObtainRequestMockMvcTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JpaContext jpaContext;

    /*private final CategoryDao[] testCategoryArrayDao = {
            testCategory("test", "\"all\".\"test-category-1\""),
            testCategory("test","\"all\".\"test-category-2\""),
            testCategory("test","\"all\".\"test-category-3\""),
            testCategory("test","\"all\".\"test-category-4\""),
            testCategory("test","\"all\".\"test-category-5\""),
            testCategory("test","\"all\".\"test-category-6\"")
    };

    @BeforeEach
    public void insertTestData(){
        Arrays.stream(testCategoryArrayDao).forEach(category -> {
            insertTestCategory(category.getName(), category.getPath());
        });
        insertTestProduct("test-product-1",
                "\"all\".\"test-category-1\"",
                "\"all\".\"test-category-2\"",
                "\"all\".\"test-category-3\"",
                "\"all\".\"test-category-4\"",
                "\"all\".\"test-category-5\""
        );
        insertTestProduct("test-product-2",
                "\"all\".\"test-category-1\"",
                "\"all\".\"test-category-4\"",
                "\"all\".\"test-category-6\""
        );
        insertTestProduct("test-product-3",
                "\"all\".\"test-category-1\"",
                "\"all\".\"test-category-4\"",
                "\"all\".\"test-category-6\""
        );
        insertTestProduct("test-product-4",
                "\"all\".\"test-category-1\"",
                "\"all\".\"test-category-2\"",
                "\"all\".\"test-category-3\"",
                "\"all\".\"test-category-4\"",
                "\"all\".\"test-category-5\"",
                "\"all\".\"test-category-6\""
        );
        insertTestReview("test-product-1");
        insertTestReview("test-product-2");
        insertTestReview("test-product-2");
        insertTestReview("test-product-2");
        insertTestReview("test-product-3");
        insertTestReview("test-product-3");
    }

    @AfterEach
    public void flushData(){
        var deletedProductsQty= jpaContext.getEntityManagerByManagedType(ProductDao.class)
                .createQuery("delete from ProductDao p where p.id is not null")
                .executeUpdate();
        var deletedCategoriesQty = jpaContext.getEntityManagerByManagedType(CategoryDao.class)
                .createQuery("delete from CategoryDao c where c.id is not null")
                .executeUpdate();
        var deletedReviewsQty = jpaContext.getEntityManagerByManagedType(ReviewDao.class)
                .createQuery("delete from ReviewDao r where r.id is not null")
                .executeUpdate();

        log.info("Flushed: '{}' Products", deletedProductsQty);
        log.info("Flushed: '{}' Categories", deletedCategoriesQty);
        log.info("Flushed: '{}' Reviews", deletedReviewsQty);
    }

    @Test
    void shouldRespondWith200AndBodyContainingListOfProductsWithIdProvidedInArgs() throws Exception {
        var content = mapper.writeValueAsString(testCoreRequestForObtainingProduct());

        var mockMvcResult = this.mockMvc.perform(post("/rq")
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mockMvcResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    private void insertTestProduct(String name, String ... categoryNames){
        var categories = findCategoriesByName(categoryNames);
        log.info("Persisting Product: {name :'{}', categories: '{}'}", name, categories);
        jpaContext.getEntityManagerByManagedType(ProductDao.class)
                .persist(testProduct(name, Set.copyOf(categories)));
    }

    private void insertTestCategory(String name, String path){
        log.info("Persisting Category: {name: '{}', path: '{}'}", name, path);
        jpaContext.getEntityManagerByManagedType(CategoryDao.class)
                .persist(testCategory(name, path));
    }

    private void insertTestReview(String productName){
        var productId = findProductIdByName(productName);

        log.info("Persisting Review: {product id :'{}'}", productId);

        jpaContext.getEntityManagerByManagedType(ReviewDao.class)
                .persist(testReview(productId, 1L, "test review", "test"));
    }

    private Long findProductIdByName(String name){
        return jpaContext.getEntityManagerByManagedType(ProductDao.class)
                .createQuery("select p.id from ProductDao p where p.name= :name", Long.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    private List<ProductDao> findExpectedProductList(){
        return jpaContext.getEntityManagerByManagedType(ProductDao.class)
                .createQuery(writeQuery(TEST_ID_ARRAY_FOR_PRODUCT_OBTAIN_REQUEST), ProductDao.class)
                .getResultList();
    }

    private List<CategoryDao> findCategoriesByName(String ... paths){
        var query = writeQuery(paths);

        return jpaContext.getEntityManagerByManagedType(CategoryDao.class)
                .createQuery(query, CategoryDao.class)
                .getResultList();
    }

    private String writeQuery(long[] idArray){
        StringBuilder query = new StringBuilder("select p from Product p ");
        if(idArray.length > 0){
            query.append("where ");
            for (int i =0; i< idArray.length; i++){
                query.append("p.id= ").append(idArray[i]).append(" ");
                if(i < idArray.length -1){
                    query.append("or ");
                }
            }
        }
        return query.toString();
    }

    private String writeQuery(String[] names){
        StringBuilder query = new StringBuilder("select c from Category c ");
        if(names.length > 0){
            query.append("where ");
            for (int i =0; i< names.length; i++){
                query.append("(c.path= '").append(names[i]).append("' and ")
                        .append("c.name= 'test') ");
                if(i < names.length -1){
                    query.append("or ");
                }
            }
        }
        return query.toString();
    }*/
}
