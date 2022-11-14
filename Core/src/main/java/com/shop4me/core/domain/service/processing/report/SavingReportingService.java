package com.shop4me.core.domain.service.processing.report;

import com.shop4me.core.domain.model.request.RequestProcessingReportImpl;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static com.shop4me.core.domain.model.request.keys.RequestProcessingStatus.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SavingReportingService {

    public static RequestProcessingReport write(Map<String, String> savingResult, Collection<String> correlationIds){
        var status = defineProcessingResult(savingResult.values());
        var resultMap = writeResultMap(savingResult, correlationIds);
        return new RequestProcessingReportImpl(status, resultMap);
    }

    protected static String defineProcessingResult(Collection<String> persistingResults){
        if(CHECK_IF_SUCCESS.test(persistingResults)){
            return SUCCESS.name();
        } else if (CHECK_IF_FAILURE.test(persistingResults)) {
            return FAILED.name();
        }else {
            return PARTLY_SUCCESS.name();
        }
    }

    private static Map<String, String> writeResultMap(Map<String, String> savingResult, Collection<String> correlationIds){
        var recordIndex = new AtomicInteger(1);
        var resultMap = new HashMap<String, String>();
        correlationIds.forEach(correlationId->{
            var index = recordIndex.getAndIncrement()+"";
            var status = savingResult.get(correlationId);
            resultMap.put(index, status);
        });
        return resultMap;
    }

    private static final Predicate<Collection<String>> CHECK_IF_SUCCESS = persistingResults->{
        var isSuccess = new AtomicBoolean();
        persistingResults.stream()
                .filter(status-> status.equals("FAILURE"))
                .findFirst()
                .ifPresentOrElse(status-> isSuccess.set(false), ()-> isSuccess.set(true));
        return isSuccess.get();
    };

    private static final Predicate<Collection<String>> CHECK_IF_FAILURE = persistingResults->{
        var isSuccess = new AtomicBoolean();
        persistingResults.stream()
                .filter(status-> status.equals("SUCCESS"))
                .findFirst()
                .ifPresentOrElse(status-> isSuccess.set(false), ()-> isSuccess.set(true));
        return isSuccess.get();
    };
}
