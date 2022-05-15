package com.shop.application.controllers;

import com.shop.application.bussineslogic.AuthService;
import com.shop.application.bussineslogic.CustomerService;
import com.shop.application.entities.auctionsdb.Customer;
import com.shop.application.entities.userdb.LoginDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor

@RestController
@RequestMapping("/customer/private")
public class PrivateCustomerController {

    private final CustomerService service;
    private final AuthService auth;

    @GetMapping("/personaldata")
    public Optional<Customer> getCustomerPersonalData(@RequestHeader("Authorization") String token){
        return service.findCustomerByCustomerID(
                auth.getCustomerIDByUsernameGotFromToken(token)
        );
    }

    @DeleteMapping("/delete/pd")
    public String deleteCustomerPersonalData(@RequestBody Customer customer){
        return service.deleteCustomerPersonalData(customer);
    }

    @DeleteMapping("/delete/login")
    public String deleteCustomerLoginDetails(@RequestBody LoginDetails details){
        return auth.deleteUser(details);
    }
}

