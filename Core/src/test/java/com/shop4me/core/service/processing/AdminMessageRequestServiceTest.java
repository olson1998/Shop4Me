package com.shop4me.core.service.processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.port.dto.InboundMsg;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.processing.MessageResponseListenersManager;
import com.shop4me.core.domain.port.web.messaging.CategoryTopicListenerRepository;
import com.shop4me.core.domain.port.web.messaging.ProductTopicListenerRepository;
import com.shop4me.core.domain.service.processing.AdminMessageRequestService;
import com.shop4me.core.domain.service.processing.utils.MessageResponseListener;
import com.shop4me.core.domain.service.processing.utils.PayloadReader;
import com.shop4me.core.domain.service.processing.utils.PayloadWriter;
import com.shop4me.core.model.dto.productdatastream.ProductGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.shop4me.core.domain.model.request.keys.RequestProcessingStatus.FAILURE;
import static com.shop4me.core.domain.model.request.keys.RequestProcessingStatus.SUCCESS;
import static com.shop4me.core.domain.service.processing.utils.PayloadTypeRef.MAP_STR_STR_REF;
import static com.shop4me.core.domain.service.processing.AdminMessageRequestService.ADMIN_SERVICE_FINGERPRINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AdminMessageRequestServiceTest {

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private ProductTopicListenerRepository productTopicListenerRepository;

    @Mock
    private CategoryTopicListenerRepository categoryTopicListenerRepository;

    @Mock
    private MessageResponseListenersManager messageResponseListenersManager;

    @Mock
    private MessageResponseListener messageResponseListener;

    @Mock
    private InboundMsg inboundMsg;

    @Mock
    private Map<String, String> resultMap;

    @Captor
    private ArgumentCaptor<Map<String, ProductDto>> productSaveMapCaptor;

    private static final int TEST_TENANT_ID = 1;

    private static final String TEST_CORRELATION_ID = UUID.randomUUID().toString();

    private static final String TEST_MAP_JSON = "{}";

    private static final String TEST_PAYLOAD_MAP = PayloadWriter.write(TEST_MAP_JSON);

    private static final ProductDto[] TEST_PRODUCTS = {
            ProductGenerator.generate("test-1"),
            ProductGenerator.generate("test-2"),
            ProductGenerator.generate("test-3")
    };

    @Test
    void shouldRegisterNewResponseListenerInListenerManager(){
        try(var messageResponseListenerMockClass = Mockito.mockStatic(MessageResponseListener.class)){
            messageResponseListenerMockClass.when(()-> MessageResponseListener.create(TEST_CORRELATION_ID))
                    .thenReturn(messageResponseListener);
            given(productTopicListenerRepository.requestSavingProduct(eq(TEST_TENANT_ID), anyMap()))
                    .willReturn(TEST_CORRELATION_ID);

            adminMessageRequestService().saveProducts(TEST_TENANT_ID, TEST_PRODUCTS).get();

            then(messageResponseListenersManager)
                    .should().registerListener(ADMIN_SERVICE_FINGERPRINT, TEST_CORRELATION_ID, messageResponseListener);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldGetActiveResponseListenerFromManagerByCorrelationIdOfGotMessage(){
        given(inboundMsg.getMessageId())
                .willReturn(TEST_CORRELATION_ID);
        given(messageResponseListenersManager.getListener(TEST_CORRELATION_ID))
                .willReturn(messageResponseListener);

        adminMessageRequestService().receiveResponse(inboundMsg);

        then(messageResponseListenersManager).should(times(2))
                .getListener(TEST_CORRELATION_ID);
        then(messageResponseListener).should().notifyReceiving();
    }

    @Test
    void shouldProductTopicListenerToSaveGivenProducts() throws ExecutionException, InterruptedException {
        try(var messageResponseListenerMockClass = Mockito.mockStatic(MessageResponseListener.class)){
            messageResponseListenerMockClass.when(()-> MessageResponseListener.create(TEST_CORRELATION_ID))
                            .thenReturn(messageResponseListener);

            adminMessageRequestService().saveProducts(TEST_TENANT_ID,TEST_PRODUCTS).get();

            then(productTopicListenerRepository).should()
                    .requestSavingProduct(eq(TEST_TENANT_ID), productSaveMapCaptor.capture());

            var requestedProductsNames = Arrays.stream(TEST_PRODUCTS)
                    .map(ProductDto::getName)
                    .toArray(String[]::new);

            var capturedProductsNames = productSaveMapCaptor.getValue().values().stream()
                    .map(ProductDto::getName)
                    .toList();

            assertThat(capturedProductsNames).containsExactlyInAnyOrder(requestedProductsNames);
        }
    }

    @Test
    void shouldReturnSuccessProcessingReportIfOutboundMessage(){
        var adminMessageRequestService = adminMessageRequestService();
        try(var messageResponseListenerMockClass = Mockito.mockStatic(MessageResponseListener.class);
            var payloadReaderMock = Mockito.mockStatic(PayloadReader.class)){
            given(productTopicListenerRepository.requestSavingProduct(eq(TEST_TENANT_ID), anyMap()))
                    .willReturn(TEST_CORRELATION_ID);
            given(messageResponseListenersManager.getListener(TEST_CORRELATION_ID))
                    .willReturn(messageResponseListener);
            given(inboundMsg.getMessageId())
                    .willReturn(TEST_CORRELATION_ID);
            given(inboundMsg.getProcessingStatus())
                    .willReturn("OK");
            given(objectMapper.readValue(TEST_MAP_JSON, MAP_STR_STR_REF))
                    .willReturn(resultMap);
            given(resultMap.get(anyString()))
                    .willReturn(SUCCESS.name());

            messageResponseListenerMockClass.when(()-> MessageResponseListener.create(TEST_CORRELATION_ID))
                    .thenReturn(messageResponseListener);
            payloadReaderMock.when(()-> PayloadReader.read(inboundMsg))
                    .thenReturn(TEST_MAP_JSON);

            adminMessageRequestService
                    .receiveResponse(inboundMsg);
            var report =adminMessageRequestService
                    .saveProducts(TEST_TENANT_ID, TEST_PRODUCTS).get();

            assertThat(report.getProcessingStatus()).isEqualTo(SUCCESS.name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnFailureProcessingReportIfOutboundMessage(){
        var adminMessageRequestService = adminMessageRequestService();
        try(var messageResponseListenerMockClass = Mockito.mockStatic(MessageResponseListener.class)){
            given(productTopicListenerRepository.requestSavingProduct(eq(TEST_TENANT_ID), anyMap()))
                    .willReturn(TEST_CORRELATION_ID);
            given(messageResponseListenersManager.getListener(TEST_CORRELATION_ID))
                    .willReturn(messageResponseListener);
            given(inboundMsg.getMessageId())
                    .willReturn(TEST_CORRELATION_ID);
            given(inboundMsg.getProcessingStatus())
                    .willReturn("ERROR");

            messageResponseListenerMockClass.when(()-> MessageResponseListener.create(TEST_CORRELATION_ID))
                    .thenReturn(messageResponseListener);

            adminMessageRequestService
                    .receiveResponse(inboundMsg);
            var report =adminMessageRequestService
                    .saveProducts(TEST_TENANT_ID, TEST_PRODUCTS).get();

            assertThat(report.getProcessingStatus()).isEqualTo("ERROR");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AdminMessageRequestService adminMessageRequestService(){
        return new AdminMessageRequestService(
                objectMapper,
                productTopicListenerRepository,
                categoryTopicListenerRepository,
                messageResponseListenersManager
        );
    }
}
