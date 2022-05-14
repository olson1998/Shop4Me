package com.shop.application.controllers;

import com.shop.application.bussineslogic.AuthService;
import com.shop.application.entities.userdb.LoginDetails;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Log4j2

@AllArgsConstructor

@RestController
public class BeansController {

    private final ApplicationContext context;
    private final AuthService authService;

    @GetMapping
    public String[] getAllBeans(){
        log.info("Returned all beans at: " + LocalDateTime.now( )+", zone: " + ZoneId.systemDefault());
        return context.getBeanDefinitionNames();
    }

    @GetMapping("/det")
    public List<LoginDetails> all(){
        return authService.all();
    }

}


