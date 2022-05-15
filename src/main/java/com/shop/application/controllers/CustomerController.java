package com.shop.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.application.bussineslogic.AuthService;
import com.shop.application.bussineslogic.CustomerService;
import com.shop.application.entities.auctionsdb.Customer;
import com.shop.application.entities.userdb.LoginDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@AllArgsConstructor

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService service;
    private final AuthService auth;

    @PostMapping("/save/login")
    public ResponseEntity<Object>  saveNewCustomerLoginDetails(@RequestBody LoginDetails details) {
        Integer granted_id = auth.saveNewUser(details);
        return ResponseEntity
                .status(HttpStatus.OK).body(Map.of(
                        "customerID", granted_id
                ));
    }

    @PostMapping("/save/personaldata")
    public ResponseEntity<Object> saveNewCustomerPersonalData(@RequestParam("username") String username,
                                                              @RequestBody Customer customer){
        String msg = service.saveNewCustomer(customer, username);
        return ResponseEntity
                .status(HttpStatus.OK).body(Map.of(
                        "message", msg
                ));
    }

}
