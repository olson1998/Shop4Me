package com.shop.application.controllers;

import com.shop.application.bussineslogic.AuthService;
import com.shop.application.bussineslogic.CustomerService;
import com.shop.application.entities.auctionsdb.Customer;
import com.shop.application.entities.userdb.LoginDetails;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService service;
    private final AuthService authService;

    @GetMapping(path = "/all")
    public List<Customer> getAllCustomers(){
        return service.getAllCustomers();
    }

    @PostMapping(path = "/login")
    public void saveNewCustomerAccount(@RequestBody LoginDetails details){
        authService.loadUserByUsername(details.getUsername());
    }

    @PostMapping("/save")
    public String saveNewCustomerLoginDetails(@RequestBody LoginDetails details){
        return authService.saveNewUser(details);
    }
}
