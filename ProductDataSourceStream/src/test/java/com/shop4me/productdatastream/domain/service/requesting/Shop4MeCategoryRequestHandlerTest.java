package com.shop4me.productdatastream.domain.service.requesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.productdatastream.domain.model.data.dto.Category;
import com.shop4me.productdatastream.domain.port.persisting.repositories.category.CategoryObtainExecutor;
import com.shop4me.productdatastream.domain.port.persisting.repositories.category.CategorySaveExecutor;
import com.shop4me.productdatastream.domain.port.requesting.CategoryObtainRequest;
import com.shop4me.productdatastream.domain.port.requesting.CategorySaveRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.shop4me.productdatastream.application.request.CategoryOperationTestRequest.categoryObtainCoreRequest;
import static com.shop4me.productdatastream.application.request.CategoryOperationTestRequest.categorySaveCoreRequest;
import static java.util.Map.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class Shop4MeCategoryRequestHandlerTest {

    @Mock
    private CategoryObtainExecutor categoryObtainExecutor;

    @Mock
    private CategorySaveExecutor categorySaveExecutor;

    @InjectMocks
    private Shop4MeCategoryRequestService shop4MeCategoryRequestService;

    private static final Map<String, Category> CATEGORY_SAVE_REQUEST_PAYLOAD= Map.ofEntries(
            entry(UUID.randomUUID().toString(), new Category(null, "\"all\"", "test"))
    );

    @Test
    void shouldDelegateTheRequestToCategorySaveExecutor() throws ExecutionException, InterruptedException{
        shop4MeCategoryRequestService
                .handle(categorySaveCoreRequest(CATEGORY_SAVE_REQUEST_PAYLOAD)).get();

        then(categorySaveExecutor).should().execute(any(CategorySaveRequest.class));
    }
}
