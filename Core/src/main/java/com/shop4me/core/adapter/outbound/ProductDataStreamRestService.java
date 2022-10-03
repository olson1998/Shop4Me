package com.shop4me.core.adapter.outbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop4me.core.adapter.outbound.exception.Shop4MeCoreNotAuthenticated;
import com.shop4me.core.application.dto.product_data_stream.Category;
import com.shop4me.core.application.dto.product_data_stream.Product;
import com.shop4me.core.application.dto.product_data_stream.Review;
import com.shop4me.core.application.requesting.Shop4MeCoreRequest;
import com.shop4me.core.domain.model.request.tool.ProductSearchFilter;
import com.shop4me.core.domain.port.web.client.DataStreamWebClient;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.shop4me.core.adapter.outbound.utils.BehavioralFunctions.ON_BAD_REQUEST;
import static com.shop4me.core.adapter.outbound.utils.PredicateHttpResponseCode.BAD_REQUEST;
import static com.shop4me.core.adapter.outbound.utils.PredicateHttpResponseCode.FORBIDDEN;
import static com.shop4me.core.domain.model.component.Shop4MeComponent.PRODUCT_DATA_STREAM;
import static com.shop4me.core.adapter.outbound.utils.TypeReference.MAP_STR_INT_REF;
import static com.shop4me.core.adapter.outbound.utils.TypeReference.MAP_STR_STR_REF;

@Slf4j

@AllArgsConstructor

@Service
public class ProductDataStreamRestService {

    private final DataStreamWebClient productDataStreamWebClient;

    private final ObjectMapper mapper;

    public Mono<Product[]> requestObtainingProducts(int[] idArray){
        var request = productObtainRequest(idArray);
        return productDataStreamWebClient.get().post()
                .uri("/rq")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(FORBIDDEN, r-> Mono.error(new Shop4MeCoreNotAuthenticated(request.toString(), PRODUCT_DATA_STREAM.name())))
                .onStatus(BAD_REQUEST, ON_BAD_REQUEST)
                .bodyToMono(Product[].class);
    }

    public Mono<Map<String, String>> requestSavingProducts(Map<String, Product> productSavingMap){
        var request = productSaveRequest(productSavingMap);
        return productDataStreamWebClient.get().post()
                .uri("/rq")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(FORBIDDEN, r-> Mono.error(new Shop4MeCoreNotAuthenticated(request.toString(), PRODUCT_DATA_STREAM.name())))
                .onStatus(BAD_REQUEST, ON_BAD_REQUEST)
                .bodyToMono(MAP_STR_STR_REF);
    }

    public Mono<Map<String, Integer>> requestEditingProducts(Map<String, String> editingProductMap){
        var request = productEditRequest(editingProductMap);
        return productDataStreamWebClient.get().post()
                .uri("/rq")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(FORBIDDEN, r-> Mono.error(new Shop4MeCoreNotAuthenticated(request.toString(), PRODUCT_DATA_STREAM.name())))
                .onStatus(BAD_REQUEST, ON_BAD_REQUEST)
                .bodyToMono(MAP_STR_INT_REF);
    }

    public Mono<Integer[]> requestSearchingProduct(List<ProductSearchFilter> productSearchFilters){
        var request = productSearchRequest(productSearchFilters);
        return productDataStreamWebClient.get().post()
                .uri("/rq")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(FORBIDDEN, r-> Mono.error(new Shop4MeCoreNotAuthenticated(request.toString(), PRODUCT_DATA_STREAM.name())))
                .onStatus(BAD_REQUEST, ON_BAD_REQUEST)
                .bodyToMono(Integer[].class);
    }

    public Mono<Map<String, Integer>> requestDeletingProduct(Product product){
        var request = productDeleteRequest(product);
        return productDataStreamWebClient.get().post()
                .uri("/rq")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(FORBIDDEN, r-> Mono.error(new Shop4MeCoreNotAuthenticated(request.toString(), PRODUCT_DATA_STREAM.name())))
                .onStatus(BAD_REQUEST, ON_BAD_REQUEST)
                .bodyToMono(MAP_STR_INT_REF);
    }

    public Mono<Map<String, String>> requestSavingReview(Map<String, Review> reviewSaveMap) {
        var request = reviewSaveRequest(reviewSaveMap);
        return productDataStreamWebClient.get().post()
                .uri("/rq")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(FORBIDDEN, r-> Mono.error(new Shop4MeCoreNotAuthenticated(request.toString(), PRODUCT_DATA_STREAM.name())))
                .onStatus(BAD_REQUEST, ON_BAD_REQUEST)
                .bodyToMono(MAP_STR_STR_REF);
    }

    public Mono<Map<String, Integer>> requestEditingReview(Map<String, String> reviewEditMap){
        var request = reviewEditRequest(reviewEditMap);
        return productDataStreamWebClient.get().post()
                .uri("/rq")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(FORBIDDEN, r-> Mono.error(new Shop4MeCoreNotAuthenticated(request.toString(), PRODUCT_DATA_STREAM.name())))
                .onStatus(BAD_REQUEST, ON_BAD_REQUEST)
                .bodyToMono(MAP_STR_INT_REF);
    }

    public Mono<Map<String, Integer>> requestDeletingReview(Review review){
        var request = reviewDeletingRequest(review);

        return productDataStreamWebClient.get().post()
                .uri("/rq")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(FORBIDDEN, r-> Mono.error(new Shop4MeCoreNotAuthenticated(request.toString(), PRODUCT_DATA_STREAM.name())))
                .onStatus(BAD_REQUEST, ON_BAD_REQUEST)
                .bodyToMono(MAP_STR_INT_REF);
    }

    public Mono<Map<String, String>> requestSavingCategory(Map<String, Category> categorySaveMap){
        var request = categorySavingRequest(categorySaveMap);
        return productDataStreamWebClient.get().post()
                .uri("/rq")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(FORBIDDEN, r-> Mono.error(new Shop4MeCoreNotAuthenticated(request.toString(), PRODUCT_DATA_STREAM.name())))
                .onStatus(BAD_REQUEST, ON_BAD_REQUEST)
                .bodyToMono(MAP_STR_STR_REF);
    }

    @SneakyThrows
    private Shop4MeCoreRequest createProductRequest(String operation, Object obj){
        var json = mapper.writeValueAsString(obj);
        var base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        return new Shop4MeCoreRequest("PRODUCT", operation, base64);
    }

    @SneakyThrows
    private Shop4MeCoreRequest createReviewRequest(String operation, Object obj){
        var json = mapper.writeValueAsString(obj);
        var base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        return new Shop4MeCoreRequest("REVIEW", operation, base64);
    }

    @SneakyThrows
    private Shop4MeCoreRequest createCategoryRequest(String operation, Object obj){
        var json = mapper.writeValueAsString(obj);
        var base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        return new Shop4MeCoreRequest("CATEGORY", operation, base64);
    }

    private Shop4MeCoreRequest productObtainRequest(int[] idArray){
        return createProductRequest("OBTAIN", idArray);
    }

    private Shop4MeCoreRequest productSaveRequest(Map<String, Product> productSavingMap){
        return createProductRequest("SAVE", productSavingMap);
    }

    private Shop4MeCoreRequest productEditRequest(Map<String, String> productEditingMap){
        return createProductRequest("EDIT", productEditingMap);
    }

    private Shop4MeCoreRequest productSearchRequest(List<ProductSearchFilter> productSearchFilters){
        return createProductRequest("SEARCH", productSearchFilters);
    }

    private Shop4MeCoreRequest productDeleteRequest(Product product){
        return createProductRequest("DELETE", product);
    }

    private Shop4MeCoreRequest reviewSaveRequest(Map<String, Review> reviewSaveMap){
        return createReviewRequest("SAVE", reviewSaveMap);
    }

    private Shop4MeCoreRequest reviewEditRequest(Map<String, String> reviewEditMap){
        return createReviewRequest("EDIT", reviewEditMap);
    }

    private Shop4MeCoreRequest reviewDeletingRequest(Review review){
        return createReviewRequest("DELETE", review);
    }

    private Shop4MeCoreRequest categorySavingRequest(Map<String, Category> categorySaveMap){
        return createCategoryRequest("SAVE", categorySaveMap);
    }
}
