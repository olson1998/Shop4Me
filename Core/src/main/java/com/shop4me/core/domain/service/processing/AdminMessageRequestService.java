package com.shop4me.core.domain.service.processing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.model.exception.InboundMessageErrorException;
import com.shop4me.core.domain.port.dto.InboundMsg;
import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import com.shop4me.core.domain.port.requesting.AdminMessageRequestRepository;
import com.shop4me.core.domain.port.web.datastream.productdatastream.ProductTopicListenerRepository;
import com.shop4me.core.domain.service.processing.report.EntityEditingReportingService;
import com.shop4me.core.domain.service.processing.report.ErrorReportingService;
import com.shop4me.core.domain.service.processing.report.SavingReportingService;
import com.shop4me.core.domain.service.utils.PayloadReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j

@RequiredArgsConstructor
public class AdminMessageRequestService implements AdminMessageRequestRepository {

    private final ObjectMapper objectMapper;

    private final ProductTopicListenerRepository productTopicListenerRepository;

    private final ConcurrentHashMap<String, MessageResponseObserver> messageResponseObserversMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, InboundMsg> receivedMessages = new ConcurrentHashMap<>();

    @Override
    public void receiveResponse(InboundMsg inboundMsg) {
        var id = inboundMsg.getMessageId();
        this.receivedMessages.put(id, inboundMsg);
        synchronized (messageResponseObserversMap.get(id)){
            try{
                messageResponseObserversMap.get(id).notifyReceiving();
            }catch (NullPointerException e){
                log.error("NO OBSERVATIONS FOR: '{}'", id);
            }
        }
    }

    @Override
    public CompletableFuture<RequestProcessingReport> saveProducts(int tenantId, ProductDto[] products) {
        var correlationIdCollRef = new AtomicReference<Collection<String>>();
        var correlationIdRef = new AtomicReference<String>();
        return CompletableFuture.supplyAsync(()-> createCorrelationIdsArray(products.length))
                .thenApply(correlationIds-> createProductSavingMap(correlationIds, products))
                .thenApply(productSaveMap-> saveCorrelationIds(correlationIdCollRef, productSaveMap))
                .thenApply(productSaveMap-> productTopicListenerRepository.requestSavingProduct(tenantId, productSaveMap))
                .thenAccept(correlationIdRef::set)
                .thenApply(empty-> createResponseObservation(correlationIdRef.get()))
                .thenAccept(MessageResponseObserver::waitUntilResponse)
                .thenApply(empty-> obtainInboundMessage(correlationIdRef.get()))
                .thenApply(this::resolveResponse)
                .thenApply(this::readResponseMap)
                .thenApply(response-> SavingReportingService.write(response, correlationIdCollRef.get()))
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> saveCategories(int tenantId, CategoryDto[] categories) {
        return null;
    }

    @Override
    public CompletableFuture<RequestProcessingReport> deleteProduct(int tenantId, ProductDto product) {
        return null;
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProduct(int tenantId, Map<String, String> productPropertyNewValueMap) {
        var correlationIdRef = new AtomicReference<String>();
        return CompletableFuture.supplyAsync(()-> productTopicListenerRepository.requestEditingProduct(tenantId, productPropertyNewValueMap))
                .thenAccept(correlationIdRef::set)
                .thenApply(empty-> createResponseObservation(correlationIdRef.get()))
                .thenAccept(MessageResponseObserver::waitUntilResponse)
                .thenApply(empty-> obtainInboundMessage(correlationIdRef.get()))
                .thenApply(this::resolveResponse)
                .thenApply(this::readResponseMap)
                .thenApply(EntityEditingReportingService::write)
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProductsCategories(int tenantId, String productId, Long[] categoriesIds) {
        return null;
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProductsImageUrls(int tenantId, String productId, String[] imageUrlsIds) {
        return null;
    }

    @SneakyThrows
    private InboundMsg resolveResponse(InboundMsg inboundMsg){
        if(inboundMsg.getProcessingStatus().equals("OK")){
            return inboundMsg;
        } else if (inboundMsg.getProcessingStatus().equals("ERROR")) {
            var payload = PayloadReader.read(inboundMsg.getPayload());
            throw new InboundMessageErrorException(inboundMsg.getHeader(), payload);
        }else {
            throw new UnknownError();
        }
    }

    private InboundMsg obtainInboundMessage(String correlationId){
        var inboundMsg = receivedMessages.get(correlationId);
        receivedMessages.remove(correlationId);
        messageResponseObserversMap.remove(correlationId);
        return inboundMsg;
    }

    private MessageResponseObserver createResponseObservation(String correlationId){
        var observer = new MessageResponseObserver(correlationId);
        messageResponseObserversMap.put(correlationId, observer);
        return observer;
    }

    private Map<String, ProductDto> saveCorrelationIds(AtomicReference<Collection<String>> correlationIdsRef,
                                                       Map<String, ProductDto> productSavingMap){
        correlationIdsRef.set(productSavingMap.keySet());
        return productSavingMap;
    }

    private Map<String, ProductDto> createProductSavingMap(String[] correlationIds,ProductDto[] productDtos){
        var productSaveMap = new HashMap<String, ProductDto>();
        for(int i=0;i < productDtos.length; i++){
            var correlationId = correlationIds[i];
            var product = productDtos[i];
            productSaveMap.put(correlationId, product);
        }
        return productSaveMap;
    }

    private String[] createCorrelationIdsArray(int qty){
        var correlationIdArray = new String[qty];
        for(int i=0;i < correlationIdArray.length; i++){
            correlationIdArray[i] = UUID.randomUUID().toString();
        }
        return correlationIdArray;
    }

    @SneakyThrows
    private Map<String, String> readResponseMap(InboundMsg inboundMsg) {
        var base64= inboundMsg.getPayload();
        var json = PayloadReader.read(base64);
        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }
}
