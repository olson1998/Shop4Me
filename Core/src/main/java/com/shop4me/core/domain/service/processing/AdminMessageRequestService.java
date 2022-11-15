package com.shop4me.core.domain.service.processing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.domain.model.dto.productdatastream.ProductRecord;
import com.shop4me.core.domain.model.exception.InboundMessageErrorException;
import com.shop4me.core.domain.model.exception.NothingToObtain;
import com.shop4me.core.domain.port.dto.InboundMsg;
import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.productdatastream.utils.ProductSearchFilterDto;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import com.shop4me.core.domain.port.dto.utils.RelationEdit;
import com.shop4me.core.domain.port.processing.MessageResponseListenersManager;
import com.shop4me.core.domain.port.requesting.AdminMessagingRequestRepository;
import com.shop4me.core.domain.port.web.messaging.CategoryTopicListenerRepository;
import com.shop4me.core.domain.port.web.messaging.ProductTopicListenerRepository;
import com.shop4me.core.domain.service.processing.report.EntityEditingReportingService;
import com.shop4me.core.domain.service.processing.report.ErrorReportingService;
import com.shop4me.core.domain.service.processing.report.SavingReportingService;
import com.shop4me.core.domain.service.processing.utils.MessageResponseListener;
import com.shop4me.core.domain.service.processing.utils.PayloadReader;
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

import static java.util.Map.entry;

@Slf4j

@RequiredArgsConstructor
public class AdminMessageRequestService implements AdminMessagingRequestRepository {

    private final ObjectMapper objectMapper;

    private final ProductTopicListenerRepository productTopicListenerRepository;

    private final CategoryTopicListenerRepository categoryTopicListenerRepository;

    private final MessageResponseListenersManager messageResponseListenersManager;
    private final ConcurrentHashMap<String, InboundMsg> receivedMessages = new ConcurrentHashMap<>();

    public static final String ADMIN_SERVICE_FINGERPRINT = UUID.randomUUID().toString();

    @Override
    public void receiveResponse(InboundMsg inboundMsg) {
        var id = inboundMsg.getMessageId();
        this.receivedMessages.put(id, inboundMsg);
        synchronized (messageResponseListenersManager.getListener(id)){
            try{
                messageResponseListenersManager.getListener(id).notifyReceiving();
            }catch (NullPointerException e){
                log.error("NO OBSERVATIONS FOR: '{}'", id);
            }
        }
    }

    @Override
    public CompletableFuture<RequestProcessingReport> saveProducts(int tenantId, ProductDto[] products) {
        log.info("ADMIN OF TENANT: '{}', REQUESTED SAVING: {} PRODUCTS",tenantId, products.length);
        var correlationIdCollRef = new AtomicReference<Collection<String>>();
        var correlationIdRef = new AtomicReference<String>();
        return CompletableFuture.supplyAsync(()-> createCorrelationIdsArray(products.length))
                .thenApply(correlationIds-> createProductSavingMap(correlationIds, products))
                .thenApply(productSaveMap-> saveCorrelationIdsOfProducts(correlationIdCollRef, productSaveMap))
                .thenApply(productSaveMap-> productTopicListenerRepository.requestSavingProduct(tenantId, productSaveMap))
                .thenAccept(correlationIdRef::set)
                .thenApply(empty-> createResponseObservation(correlationIdRef.get()))
                .thenAccept(MessageResponseListener::waitUntilResponse)
                .thenApply(empty-> obtainInboundMessage(correlationIdRef.get()))
                .thenApply(this::resolveResponse)
                .thenApply(PayloadReader::read)
                .thenApply(this::readResponseMap)
                .thenApply(response-> SavingReportingService.write(response, correlationIdCollRef.get()))
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<ProductDto[]> searchAndObtainProducts(int tenantId, ProductSearchFilterDto[] searchFilters) {
        log.info("ADMIN OF TENANT: '{}', REQUESTED SEARCHING PRODUCT: {} ", tenantId, searchFilters);
        var correlationIdRef = new AtomicReference<String>();
        return CompletableFuture.supplyAsync(()-> productTopicListenerRepository.requestSearchingProduct(tenantId, searchFilters))
                .thenAccept(correlationIdRef::set)
                .thenApply(empty-> createResponseObservation(correlationIdRef.get()))
                .thenAccept(MessageResponseListener::waitUntilResponse)
                .thenApply(empty-> obtainInboundMessage(correlationIdRef.get()))
                .thenApply(this::resolveResponse)
                .thenApply(PayloadReader::read)
                .thenApply(this::readResponseProductIds)
                .thenApply(ids-> productTopicListenerRepository.requestObtainingProducts(tenantId, ids))
                .thenAccept(correlationIdRef::set)
                .thenApply(empty-> createResponseObservation(correlationIdRef.get()))
                .thenAccept(MessageResponseListener::waitUntilResponse)
                .thenApply(empty-> obtainInboundMessage(correlationIdRef.get()))
                .thenApply(this::resolveResponse)
                .thenApply(PayloadReader::read)
                .thenApply(this::readResponseProductsArray)
                .exceptionally(this::exceptionallyReturnEmptyArray);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> saveCategories(int tenantId, CategoryDto[] categories) {
        log.info("ADMIN OF TENANT: '{}', REQUESTED SAVING: {} CATEGORIES", tenantId, categories.length);
        var correlationIdCollRef = new AtomicReference<Collection<String>>();
        var correlationIdRef = new AtomicReference<String>();
        return CompletableFuture.supplyAsync(()-> createCorrelationIdsArray(categories.length))
                .thenApply(correlationIds-> createCategoriesSavingMap(correlationIds, categories))
                .thenApply(categorySaveMap-> saveCorrelationIdsOfCategories(correlationIdCollRef, categorySaveMap))
                .thenApply(categorySaveMap-> categoryTopicListenerRepository.requestSavingCategories(tenantId, categorySaveMap))
                .thenAccept(correlationIdRef::set)
                .thenApply(empty-> createResponseObservation(correlationIdRef.get()))
                .thenAccept(MessageResponseListener::waitUntilResponse)
                .thenApply(empty-> obtainInboundMessage(correlationIdRef.get()))
                .thenApply(this::resolveResponse)
                .thenApply(PayloadReader::read)
                .thenApply(this::readResponseMap)
                .thenApply(response-> SavingReportingService.write(response, correlationIdCollRef.get()))
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> deleteProduct(int tenantId, ProductDto product) {
        return null;
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProduct(int tenantId, Map<String, String> productPropertyNewValueMap) {
        log.info("ADMIN OF TENANT: '{}', REQUESTED EDITING: {}", tenantId, productPropertyNewValueMap.keySet());
        var correlationIdRef = new AtomicReference<String>();
        return CompletableFuture.supplyAsync(()-> productTopicListenerRepository.requestEditingProduct(tenantId, productPropertyNewValueMap))
                .thenAccept(correlationIdRef::set)
                .thenApply(empty-> createResponseObservation(correlationIdRef.get()))
                .thenAccept(MessageResponseListener::waitUntilResponse)
                .thenApply(empty-> obtainInboundMessage(correlationIdRef.get()))
                .thenApply(this::resolveResponse)
                .thenApply(PayloadReader::read)
                .thenApply(this::readResponseMap)
                .thenApply(EntityEditingReportingService::write)
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProductsCategories(int tenantId, String productId, RelationEdit[] relationEdits) {
        log.info("ADMIN OF TENANT: '{}', REQUESTED CHANGING RELATIONS OF PRODUCT '{}' WITH CATEGORIES: {}",
                tenantId,
                productId,
                relationEdits
        );
        var correlationIdRef = new AtomicReference<String>();
        return CompletableFuture.supplyAsync(()-> createProductCategoriesEditMap(productId, relationEdits))
                .thenApply(productEditMap-> productTopicListenerRepository.requestEditingProduct(tenantId, productEditMap))
                .thenAccept(correlationIdRef::set)
                .thenApply(empty-> createResponseObservation(correlationIdRef.get()))
                .thenAccept(MessageResponseListener::waitUntilResponse)
                .thenApply(empty-> obtainInboundMessage(correlationIdRef.get()))
                .thenApply(this::resolveResponse)
                .thenApply(PayloadReader::read)
                .thenApply(this::readResponseMap)
                .thenApply(EntityEditingReportingService::write)
                .exceptionally(ErrorReportingService::error);
    }

    @Override
    public CompletableFuture<RequestProcessingReport> editProductsImageUrls(int tenantId, String productId, RelationEdit[] relationEdits) {
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
        messageResponseListenersManager.unregisterListener(correlationId);
        return inboundMsg;
    }

    private MessageResponseListener createResponseObservation(String correlationId){
        var listener = new MessageResponseListener(correlationId);
        messageResponseListenersManager.registerListener(
                ADMIN_SERVICE_FINGERPRINT,
                correlationId,
                listener
        );
        return listener;
    }

    private ProductDto[] exceptionallyReturnEmptyArray(Throwable e){
        var error = e.getCause();
        if (!(error instanceof NothingToObtain)) {
            log.warn("ERROR DURING OBTAINING PRODUCTS: '{}' MESSAGE: {}", error, error.getMessage());
        }
        return new ProductDto[0];
    }

    private Map<String, ProductDto> saveCorrelationIdsOfProducts(AtomicReference<Collection<String>> correlationIdsRef,
                                                       Map<String, ProductDto> productSavingMap){
        correlationIdsRef.set(productSavingMap.keySet());
        return productSavingMap;
    }

    private Map<String, CategoryDto> saveCorrelationIdsOfCategories(AtomicReference<Collection<String>> correlationIdsRef,
                                                       Map<String, CategoryDto> categoriesSavingMap){
        correlationIdsRef.set(categoriesSavingMap.keySet());
        return categoriesSavingMap;
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

    private Map<String, CategoryDto> createCategoriesSavingMap(String[] correlationIds, CategoryDto[] categoryDtos){
        var categorySaveMap = new HashMap<String, CategoryDto>();
        for(int i=0;i < categoryDtos.length; i++){
            var correlationId = correlationIds[i];
            var product = categoryDtos[i];
            categorySaveMap.put(correlationId, product);
        }
        return categorySaveMap;
    }

    private String[] createCorrelationIdsArray(int qty){
        var correlationIdArray = new String[qty];
        for(int i=0;i < correlationIdArray.length; i++){
            correlationIdArray[i] = UUID.randomUUID().toString();
        }
        return correlationIdArray;
    }

    @SneakyThrows
    private Map<String, String> createProductCategoriesEditMap(String productId, RelationEdit[] relationEdits){
        var categoriesJson = objectMapper.writeValueAsString(relationEdits);
        return Map.ofEntries(
                entry("ID", productId),
                entry("CATEGORY", categoriesJson)
        );
    }

    @SneakyThrows
    private Map<String, String> readResponseMap(String json) {
        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }

    @SneakyThrows
    private long[] readResponseProductIds(String json){
        var ids = objectMapper.readValue(json, long[].class);
        if(ids == null || ids.length ==0){
            throw new NothingToObtain();
        }else {
            return ids;
        }
    }

    @SneakyThrows
    private ProductDto[] readResponseProductsArray(String json){
        return objectMapper.readValue(json, ProductRecord[].class);
    }
}
