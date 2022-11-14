package com.shop4me.core.adapter.inbound.advicer;

import com.shop4me.core.adapter.inbound.exception.UnknownApplicationVersionException;
import com.shop4me.core.adapter.inbound.exception.UnreadableTenantIdentificationException;
import com.shop4me.core.domain.model.exception.WebClientNotSetUpException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static java.util.Map.entry;

@ControllerAdvice
public class WebClientNotSetUpExceptionHandler {

    @ExceptionHandler(WebClientNotSetUpException.class)
    public ResponseEntity<Map<String, String>> responseWith404OnWebClientNotSetUp(
            WebClientNotSetUpException e
    ){
        return ResponseEntity.badRequest()
                .body(createErrorMap("no connection..."));
    }

    @ExceptionHandler(UnknownApplicationVersionException.class)
    public ResponseEntity<Map<String, String>> responseWith400OnUnknownVersion(UnknownApplicationVersionException e){
        return ResponseEntity.badRequest()
                .body(createErrorMap(e.toString()));
    }

    @ExceptionHandler(UnreadableTenantIdentificationException.class)
    public ResponseEntity<Map<String, String>> responseWith400OnUnreadTenant(UnreadableTenantIdentificationException e){
        return ResponseEntity.badRequest()
                .body(createErrorMap(e.getMessage()));
    }

    private Map<String, String> createErrorMap(String message){
        return Map.ofEntries(
                entry("error", message)
        );
    }
}
