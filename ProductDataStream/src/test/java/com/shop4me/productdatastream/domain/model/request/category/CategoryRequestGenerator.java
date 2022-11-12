package com.shop4me.productdatastream.domain.model.request.category;

import com.shop4me.productdatastream.application.PayloadWriter;
import com.shop4me.productdatastream.application.messaging.InboundMessageGenerator;
import com.shop4me.productdatastream.domain.model.dto.Category;
import com.shop4me.productdatastream.domain.port.requesting.CategorySaveRequest;

import java.util.Map;

import static com.shop4me.productdatastream.domain.model.request.enumset.Operation.SAVE;

public class CategoryRequestGenerator {

    private static final String CATEGORY = "CATEGORY";

    private static final String CATEGORY_TEST_TOPIC ="CATEGORY-TEST-TOPIC";

    public static CategorySaveRequest categorySaveRequest(int tenantId, Map<String, Category> categorySaveMap){
        var payload = PayloadWriter.write(categorySaveMap);
        var inbound = InboundMessageGenerator.generate(CATEGORY_TEST_TOPIC, tenantId, SAVE, payload);
        return CategorySaveRequestImpl.fromInboundMessage(inbound);
    }
}
