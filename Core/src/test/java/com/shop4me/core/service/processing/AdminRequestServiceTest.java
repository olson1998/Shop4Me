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

    @Test
    void shouldDelegateProductRepositoryToSaveProducts(){
        try(var uuidClassMock = Mockito.mockStatic(UUID.class)){
            uuidClassMock.when(UUID::randomUUID).thenReturn(productSavingCorrelationId);
            given(productSavingCorrelationId.toString())
                    .willReturn(PRODUCT_1_SAVING_CORRELATION_ID, PRODUCT_2_SAVING_CORRELATION_ID);
            given(productRepository.requestSavingProducts(any()))
                    .willReturn(Mono.just(PRODUCT_SAVING_SUCCESS_RESPONSE));

            adminRequestService().saveProducts(PRODUCTS_TO_SAVE).get();

            then(productRepository).should().requestSavingProducts(any());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnSucceededReportIfProductRepositorySavedProducts(){
        try(var uuidClassMock = Mockito.mockStatic(UUID.class)){
            uuidClassMock.when(UUID::randomUUID).thenReturn(productSavingCorrelationId);
            given(productSavingCorrelationId.toString())
                    .willReturn(PRODUCT_1_SAVING_CORRELATION_ID, PRODUCT_2_SAVING_CORRELATION_ID);
            given(productRepository.requestSavingProducts(any()))
                    .willReturn(Mono.just(PRODUCT_SAVING_SUCCESS_RESPONSE));

            var report =
                    adminRequestService().saveProducts(PRODUCTS_TO_SAVE).get();

            assertThat(report.getProcessingStatus())
                    .isEqualTo(SUCCESS.name());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnFailedReportIfProductRepositorySavedProducts(){
        try(var uuidClassMock = Mockito.mockStatic(UUID.class)){
            uuidClassMock.when(UUID::randomUUID).thenReturn(productSavingCorrelationId);
            given(productSavingCorrelationId.toString())
                    .willReturn(PRODUCT_1_SAVING_CORRELATION_ID, PRODUCT_2_SAVING_CORRELATION_ID);
            given(productRepository.requestSavingProducts(any()))
                    .willReturn(Mono.just(PRODUCT_SAVING_FAILURE_RESPONSE));

            var report =
                    adminRequestService().saveProducts(PRODUCTS_TO_SAVE).get();

            assertThat(report.getProcessingStatus())
                    .isEqualTo(FAILED.name());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnPartlySucceededReportIfProductRepositorySavedProducts(){
        try(var uuidClassMock = Mockito.mockStatic(UUID.class)){
            uuidClassMock.when(UUID::randomUUID).thenReturn(productSavingCorrelationId);
            given(productSavingCorrelationId.toString())
                    .willReturn(PRODUCT_1_SAVING_CORRELATION_ID, PRODUCT_2_SAVING_CORRELATION_ID);
            given(productRepository.requestSavingProducts(any()))
                    .willReturn(Mono.just(PRODUCT_SAVING_PARTLY_SUCCESS_RESPONSE));

            var report =
                    adminRequestService().saveProducts(PRODUCTS_TO_SAVE).get();

            assertThat(report.getProcessingStatus())
                    .isEqualTo(PARTLY_SUCCESS.name());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldDelegateProductRepositoryToEditProductCategories() throws ExecutionException, InterruptedException {
        given(productRepository.requestEditingProduct(any()))
                .willReturn(Mono.just(AFFECTED_ROWS_ONE));

        adminRequestService()
                .editProductsCategories("1", EDITED_PRODUCTS_CATEGORIES_IDS).get();

        then(productRepository).should().requestEditingProduct(any());
    }

    @Test
    void shouldSendProductEditMapWithGivenParameters() throws ExecutionException, InterruptedException {
        given(productRepository.requestEditingProduct(any()))
                .willReturn(Mono.just(AFFECTED_ROWS_ONE));

        adminRequestService()
                .editProductsCategories("1", EDITED_PRODUCTS_CATEGORIES_IDS).get();

        then(productRepository).should().requestEditingProduct(mapStrStrCaptor.capture());

        var productEditMap = mapStrStrCaptor.getValue();

        assertThat(productEditMap).containsOnly(
                entry("ID", "1"),
                entry("CATEGORY", "[1,2,3]")
        );
    }

    @Test
    void shouldDelegateProductDeletingServiceToDeleteProduct() throws ExecutionException, InterruptedException {
        given(productRepository.requestDeletingProduct(any()))
                .willReturn(Mono.just(AFFECTED_ROWS_ONE));

        adminRequestService().deleteProduct(PRODUCTS_TO_SAVE[0]).get();

        then(productRepository).should().requestDeletingProduct(PRODUCTS_TO_SAVE[0]);
    }

    @Test
    void shouldReturnSucceededReportIfProductRepositoryDeletedProduct() throws ExecutionException, InterruptedException {
        given(productRepository.requestDeletingProduct(any()))
                .willReturn(Mono.just(AFFECTED_ROWS_ONE));

        var report =
                adminRequestService().deleteProduct(PRODUCTS_TO_SAVE[0]).get();

        assertThat(report.getProcessingStatus())
                .isEqualTo("SUCCESS");
    }

    @Test
    void shouldReturnFailedReportIfProductRepositoryDidNotDeleteProduct() throws ExecutionException, InterruptedException {
        given(productRepository.requestDeletingProduct(any()))
                .willReturn(Mono.just(AFFECTED_ROWS_ZERO));

        var report =
                adminRequestService().deleteProduct(PRODUCTS_TO_SAVE[0]).get();

        assertThat(report.getProcessingStatus())
                .isEqualTo(FAILED.name());
    }

    @Test
    void shouldDelegateCategoryResponseToSaveCategories() throws ExecutionException, InterruptedException {
        given(categoryRepository.saveCategories(any()))
                .willReturn(Mono.just(CATEGORIES_SAVE_SUCCESS_RESPONSE));

        adminRequestService().saveCategories(CATEGORIES_TO_SAVE).get();

        then(categoryRepository).should().saveCategories(any());
    }

    @Test
    void shouldReturnSucceededReportIfCategoryRepositorySavedCategories() throws ExecutionException, InterruptedException {
        given(categoryRepository.saveCategories(any()))
                .willReturn(Mono.just(CATEGORIES_SAVE_SUCCESS_RESPONSE));

        var report = adminRequestService()
                .saveCategories(CATEGORIES_TO_SAVE).get();

        assertThat(report.getProcessingStatus()).isEqualTo(SUCCESS.name());
    }

    @Test
    void shouldReturnFailureReportIfCategoryRepositorySavedCategories() throws ExecutionException, InterruptedException {
        given(categoryRepository.saveCategories(any()))
                .willReturn(Mono.just(CATEGORIES_SAVE_FAILED_RESPONSE));

        var report = adminRequestService()
                .saveCategories(CATEGORIES_TO_SAVE).get();

        assertThat(report.getProcessingStatus()).isEqualTo(FAILED.name());
    }

    @Test
    void shouldReturnPartlySuccessReportIfSomeOfCategoriesWereSaved() throws ExecutionException, InterruptedException {
        given(categoryRepository.saveCategories(any()))
                .willReturn(Mono.just(CATEGORIES_SAVE_PARTLY_SUCCESS_RESPONSE));

        var report = adminRequestService()
                .saveCategories(CATEGORIES_TO_SAVE).get();

        assertThat(report.getProcessingStatus()).isEqualTo(PARTLY_SUCCESS.name());
    }

    private AdminRequestService adminRequestService(){
        return new AdminRequestService(
                mapper,
                productRepository,
                categoryRepository
        );
    }
}
