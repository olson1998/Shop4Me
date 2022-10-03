package com.shop4me.productdatastream.adapter.inbound.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class UsernameNotFoundExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundExc(UsernameNotFoundExceptionHandler ex, WebRequest request){
        var unsuccessfulAuthenticationTokenMap =
                new HashMap<String, String>();
        unsuccessfulAuthenticationTokenMap.put("access_token", null);
        return ResponseEntity.ok(unsuccessfulAuthenticationTokenMap);
    }
}
