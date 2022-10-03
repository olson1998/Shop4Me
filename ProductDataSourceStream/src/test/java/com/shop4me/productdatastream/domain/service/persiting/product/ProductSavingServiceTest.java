package com.shop4me.productdatastream.domain.service.persiting.product;

import com.shop4me.productdatastream.domain.model.data.dto.Product;
import com.shop4me.productdatastream.domain.model.data.entities.productdatastorage.ProductEntity;
import com.shop4me.productdatastream.domain.port.persisting.dao.ProductDao;
import com.shop4me.productdatastream.domain.port.persisting.dto.entity.ProductDto;
import com.shop4me.productdatastream.domain.service.persisting.product.ProductSavingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

import java.util.*;

import static com.shop4me.productdatastream.domain.model.request.ProductOperationRequestTestImpl.productSaveRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductSavingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Captor
    private ArgumentCaptor<ProductDao> persistedProductDaoCaptor;

    private final Product[] testProductToSaveArray = {
            new Product(null, "test-product-1", null, null, null, null, null),
            new Product(null, "test-product-2", null, null, null, null, null),
            new Product(null, "test-product-3", null, null, null, null, null)
    };

    @Test
    void shouldPersistProductsDtosFromMap(){
        var productSaveMap = createProductSaveMap(testProductToSaveArray);

        productSavingService().execute(productSaveRequest(productSaveMap));

        productSaveMap.values().forEach(productDto -> {
            then(entityManager).should(times(testProductToSaveArray.length))
                    .persist(persistedProductDaoCaptor.capture());
        });

        var persistedProductsNames = persistedProductDaoCaptor.getAllValues().stream()
                .map(ProductDao::getName)
                .toList();

        var expectedProductsNames = productSaveMap.values().stream()
                .map(ProductDto::getName)
                .toList();

        assertThat(persistedProductsNames).containsAll(expectedProductsNames);
    }

    @Test
    void shouldReturnMapOfCorrelationIdsAndExecutionStatusSuccessIfNoExceptionOccurred(){
        var productSaveMap = createProductSaveMap(testProductToSaveArray);

        var resultMap = productSavingService()
                .execute(productSaveRequest(productSaveMap));

        assertThat(resultMap.values()).allMatch(e -> e.equals("SUCCESS"));
    }

    @Test
    void shouldReturnMapOfCorrelationIdsAndExecutionStatusFailureIfExceptionOccurred(){
        var productSaveMap = createProductSaveMap(testProductToSaveArray);

        doThrow(EntityExistsException.class).when(entityManager).persist(any(ProductEntity.class));

        var resultMap = productSavingService()
                .execute(productSaveRequest(productSaveMap));

        assertThat(resultMap.values()).allMatch(e -> e.equals("FAILURE"));
    }

    private ProductSavingService productSavingService(){
        return new ProductSavingService(entityManager);
    }

    private Map<String, ProductDto> createProductSaveMap(Product ... products){
        var productSaveMap = new HashMap<String, ProductDto>();

        Arrays.stream(products).forEach(product -> {
            productSaveMap.put(UUID.randomUUID().toString(), product);
        });
        return productSaveMap;
    }
}
