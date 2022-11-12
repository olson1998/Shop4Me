package com.shop4me.productdatastream.domain.model.request.product;

import com.shop4me.productdatastream.application.PayloadWriter;
import com.shop4me.productdatastream.application.messaging.InboundMessageGenerator;
import com.shop4me.productdatastream.domain.model.dao.productdatastorage.properties.ProductProperty;
import com.shop4me.productdatastream.domain.model.dto.Product;
import com.shop4me.productdatastream.domain.model.request.product.utils.ProductSearchFilter;
import com.shop4me.productdatastream.domain.port.requesting.*;

import java.util.Map;

import static com.shop4me.productdatastream.domain.model.request.enumset.Operation.*;

public class ProductRequestGenerator {

    private static final String PRODUCT_TEST_TOPIC = "PERSON-TEST-TOPIC";

    public static ProductSaveRequest productSaveRequest(int tenantId, Map<String, Product> productSaveMap){
        var payload = PayloadWriter.write(productSaveMap);
        var inbound = InboundMessageGenerator.generate(PRODUCT_TEST_TOPIC, tenantId, SAVE, payload);
        return ProductSaveRequestImpl.fromInboundMessage(inbound);
    }

    public static ProductObtainRequest productObtainRequest(int tenantId, long[]productIdArray){
        var payload = PayloadWriter.write(productIdArray);
        var inbound = InboundMessageGenerator.generate(PRODUCT_TEST_TOPIC, tenantId, OBTAIN, payload);
        return ProductObtainRequestImpl.fromInboundMessage(inbound);
    }

    public static ProductEditRequest productEditRequest(int tenantId, Map<ProductProperty, String> productPropertyEditMap){
        var payload = PayloadWriter.write(productPropertyEditMap);
        var inbound = InboundMessageGenerator.generate(PRODUCT_TEST_TOPIC, tenantId, EDIT, payload);
        return ProductEditRequestImpl.fromInboundMessage(inbound);
    }

    public static ProductSearchRequest productSearchRequest(int tenantId, ProductSearchFilter[] productSearchFilters){
        var payload = PayloadWriter.write(productSearchFilters);
        var inbound = InboundMessageGenerator.generate(PRODUCT_TEST_TOPIC, tenantId, SEARCH, payload);
        return ProductSearchRequestImpl.fromInboundMessage(inbound);
    }

    public static ProductDeleteRequest productDeleteRequest(int tenantId, long[] productsIdArray){
        var payload = PayloadWriter.write(productsIdArray);
        var inbound = InboundMessageGenerator.generate(PRODUCT_TEST_TOPIC, tenantId, DELETE, payload);
        return ProductDeleteRequestImpl.fromInboundMessage(inbound);
    }
}
