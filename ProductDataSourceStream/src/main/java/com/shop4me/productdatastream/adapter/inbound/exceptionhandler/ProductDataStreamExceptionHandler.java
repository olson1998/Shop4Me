package com.shop4me.productdatastream.adapter.inbound.exceptionhandler;

import com.shop4me.productdatastream.domain.model.exception.Shop4MeCoreRequestExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

import static java.util.Map.entry;

@ControllerAdvice
public class ProductDataStreamExceptionHandler extends
        ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Shop4MeCoreRequestExecutionException.class})
    public ResponseEntity<Map<String, String>> handleCoreRequestExecutionException(Shop4MeCoreRequestExecutionException ex,
                                                                      WebRequest request){
        return ResponseEntity.badRequest().body(Map.ofEntries(
                entry("exception", ex.getMessage())
        ));
    }

}
