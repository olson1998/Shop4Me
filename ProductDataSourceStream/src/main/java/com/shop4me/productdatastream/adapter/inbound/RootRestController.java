package com.shop4me.productdatastream.adapter.inbound;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor

@RestController
public class RootRestController {

    @GetMapping
    public String ping(){
        return "OK";
    }
}
