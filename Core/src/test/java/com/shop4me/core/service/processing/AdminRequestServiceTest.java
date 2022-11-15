package com.shop4me.core.service.processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.port.web.datastream.productdatastream.CategoryRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ProductRepository;
import com.shop4me.core.domain.service.processing.AdminRequestService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.shop4me.core.domain.model.request.keys.RequestProcessingStatus.*;
import static com.shop4me.core.model.ConstantTestDto.CATEGORIES_TO_SAVE;
import static com.shop4me.core.model.ConstantTestDto.PRODUCTS_TO_SAVE;
import static com.shop4me.core.model.ConstantTestResponses.*;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdminRequestServiceTest {

    @Spy
    private ObjectMapper mapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UUID productSavingCorrelationId;

    @Captor
    private ArgumentCaptor<Map<String, String>> mapStrStrCaptor;

    private static final String PRODUCT_1_SAVING_CORRELATION_ID = UUID.randomUUID().toString();

    private static final String PRODUCT_2_SAVING_CORRELATION_ID = UUID.randomUUID().toString();

    private static final Long[] EDITED_PRODUCTS_CATEGORIES_IDS = {1L,2L,3L};

    private static final Map<String, String> PRODUCT_SAVING_SUCCESS_RESPONSE = Map.ofEntries(
            entry(PRODUCT_1_SAVING_CORRELATION_ID, "SUCCESS"),
            entry(PRODUCT_2_SAVING_CORRELATION_ID, "SUCCESS")
    );

    private static final Map<String, String> PRODUCT_SAVING_FAILURE_RESPONSE = Map.ofEntries(
            entry(PRODUCT_1_SAVING_CORRELATION_ID, "FAILURE"),
            entry(PRODUCT_2_SAVING_CORRELATION_ID, "FAILURE")
    );

    private static final Map<String, String> PRODUCT_SAVING_PARTLY_SUCCESS_RESPONSE = Map.ofEntries(
            entry(PRODUCT_1_SAVING_CORRELATION_ID, "SUCCESS"),
            entry(PRODUCT_2_SAVING_CORRELATION_ID, "FAILURE")
    );

    private AdminRequestService adminRequestService(){
        return new AdminRequestService(
                mapper,
                productRepository,
                categoryRepository
        );
    }
}
