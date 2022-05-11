package com.shop.application.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2

@AllArgsConstructor

@RestController
public class BeansController {

    private final ApplicationContext context;

    @GetMapping
    public String[] getAllBeans(){
        log.info("Returned all beans at: " + LocalDateTime.now( )+", zone: " + ZoneId.systemDefault());
        return context.getBeanDefinitionNames();
    }
}
