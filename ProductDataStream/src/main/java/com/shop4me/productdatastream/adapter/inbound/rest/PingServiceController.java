package com.shop4me.productdatastream.adapter.inbound.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingServiceController {

    @GetMapping("/")
    public String ping(){
        return "OK";
    }
}
