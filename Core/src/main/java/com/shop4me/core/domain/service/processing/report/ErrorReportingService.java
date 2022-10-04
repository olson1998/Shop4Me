package com.shop4me.core.domain.service.processing.report;

import com.shop4me.core.domain.model.request.RequestProcessingReportImpl;
import com.shop4me.core.domain.model.request.keys.RequestProcessingStatus;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorReportingService {

    public static RequestProcessingReport error(Throwable e){
        log.warn(e.getMessage());
        return new RequestProcessingReportImpl(
                RequestProcessingStatus.ERROR.name(),
                e.getMessage()
        );
    }
}
