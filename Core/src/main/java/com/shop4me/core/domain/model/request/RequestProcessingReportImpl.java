package com.shop4me.core.domain.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop4me.core.domain.port.dto.response.RequestProcessingReport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter

@AllArgsConstructor

@NoArgsConstructor
public class RequestProcessingReportImpl implements RequestProcessingReport {

    @JsonProperty(value = "processing_status", access = JsonProperty.Access.READ_ONLY)
    private String processingStatus;

    @JsonProperty(value = "message", access = JsonProperty.Access.READ_ONLY)
    private String msg = "OK";

    @JsonProperty(value = "processing_status_map", access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<Object, String> processingDetails;

    public RequestProcessingReportImpl(String processingStatus) {
        this.processingStatus = processingStatus;
    }

    public RequestProcessingReportImpl(String processingStatus, String msg) {
        this.processingStatus = processingStatus;
        this.msg = msg;
    }

    public RequestProcessingReportImpl(String processingStatus, Map<Object, String> processingDetails) {
        this.processingStatus = processingStatus;
        this.processingDetails = processingDetails;
    }
}
