package com.shop4me.core.domain.service.processing.report;

import com.shop4me.core.domain.model.request.RequestProcessingReportImpl;
import com.shop4me.core.domain.model.request.keys.RequestProcessingStatus;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AffectedRowsReportingServing {

    public static RequestProcessingReport affectedRowsReport(Map<String, Integer> affectedRowsMap){
        var affectedRows = affectedRowsMap.get("affected_rows");

        if(affectedRows == 1){
            return new RequestProcessingReportImpl(RequestProcessingStatus.SUCCESS.name());
        } else if (affectedRows ==0) {
            return new RequestProcessingReportImpl(RequestProcessingStatus.FAILED.name());
        }else if (affectedRows == null){
            return new RequestProcessingReportImpl(RequestProcessingStatus.UNKNOWN.name());
        }else {
            return new RequestProcessingReportImpl(RequestProcessingStatus.UNKNOWN.name());
        }
    }
}
