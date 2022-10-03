package com.shop4me.core.adapter.inbound.advicer;

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
                .body(Map.ofEntries(
                        entry("exception", "no connection...")
                ));
    }
}
