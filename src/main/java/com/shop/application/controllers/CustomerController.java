package com.shop.application.controllers;

import com.shop.application.bussineslogic.CustomerService;
import com.shop.application.entities.Customer;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    @GetMapping(path = "/all")
    public List<Customer> getAllCustomers(){
        return service.getAllCustomers();
    }
}
