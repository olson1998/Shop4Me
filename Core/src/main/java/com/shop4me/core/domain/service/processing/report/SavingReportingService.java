package com.shop4me.core.domain.service.processing.report;

import com.shop4me.core.domain.model.request.RequestProcessingReportImpl;
import com.shop4me.core.domain.model.request.keys.RequestProcessingStatus;
import com.shop4me.core.domain.port.dto.productdatastream.CategoryDto;
import com.shop4me.core.domain.port.dto.productdatastream.ProductDto;
import com.shop4me.core.domain.port.dto.productdatastream.ReviewDto;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static com.shop4me.core.domain.service.processing.report.utils.ResponseStatusPredicationService.ONLY_FAILURE_RESPONSES;
import static com.shop4me.core.domain.service.processing.report.utils.ResponseStatusPredicationService.ONLY_SUCCESS_RESPONSES;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SavingReportingService {

    public static RequestProcessingReport categorySavingReport(Map< String, CategoryDto> categorySavingMap, Map<String, String> response){
        var categorySavingStatusMap = writeCategorySavingDetailsMap(categorySavingMap, response);
        return writeRequestProcessingReport(categorySavingStatusMap, response);
    }

    public static RequestProcessingReport productSavingReport(Map<String, ProductDto> productSavingMap, Map<String, String> response){
        var productSavingStatusMap = writeProductSavingDetailsMap(productSavingMap, response);
        return writeRequestProcessingReport(productSavingStatusMap, response);
    }

    public static RequestProcessingReport reviewSavingReport(Map<String, ReviewDto> reviewSavingMap, Map<String, String> response){
        var reviewSavinMap= writeReviewSavingDetailsMap(reviewSavingMap, response);
        return writeRequestProcessingReport(reviewSavinMap, response);
    }

    private static RequestProcessingReport writeRequestProcessingReport( Map<Object, String> dtoStatusMap, Map<String, String> response) {
        if(ONLY_SUCCESS_RESPONSES.test(response)){
            return new RequestProcessingReportImpl(
                    RequestProcessingStatus.SUCCESS.name(),
                    dtoStatusMap
            );
        }else if(ONLY_FAILURE_RESPONSES.test(response)){
            return new RequestProcessingReportImpl(
                    RequestProcessingStatus.FAILED.name(),
                    dtoStatusMap
            );
        }else {
            return new RequestProcessingReportImpl(
                    RequestProcessingStatus.PARTLY_SUCCESS.name(),
                    dtoStatusMap
            );
        }
    }

    private static Map<Object, String> writeCategorySavingDetailsMap(Map< String, CategoryDto> categorySavingMap, Map<String, String> response){
        var reportMap = new HashMap<Object, String>();
        response.forEach((correlationId, status)->{
            var category = categorySavingMap.get(correlationId);

            reportMap.put(category, status);
        });
        return reportMap;
    }

    public static Map<Object, String> writeProductSavingDetailsMap(Map<String, ProductDto> productSavingMap, Map<String, String> response){
        var reportMap = new HashMap<Object, String>();
        response.forEach((correlationId, status)->{
            var product = productSavingMap.get(correlationId);

            reportMap.put(product, status);
        });
        return reportMap;
    }

    public static Map<Object, String> writeReviewSavingDetailsMap(Map<String, ReviewDto> reviewSavingMap, Map<String, String> response){
        var reportMap = new HashMap<Object, String>();
        response.forEach((correlationId, status)->{
            var product = reviewSavingMap.get(correlationId);

            reportMap.put(product, status);
        });
        return reportMap;
    }
}
