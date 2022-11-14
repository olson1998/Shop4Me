package com.shop4me.core.domain.service.processing.report;

import com.shop4me.core.domain.model.request.RequestProcessingReportImpl;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;

import java.util.Map;

import static com.shop4me.core.domain.service.processing.report.SavingReportingService.defineProcessingResult;

public class EntityEditingReportingService {

    public static RequestProcessingReport write(Map<String, String> editResultMap){
        var status = defineProcessingResult(editResultMap.values());
        return new RequestProcessingReportImpl(status, editResultMap);
    }
}
