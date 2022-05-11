package com.shop.application.controllers;

import com.shop.application.bussineslogic.CustomerSessionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor

@RestController
@RequestMapping(path = "/sessions")
public class CustomersSessionsController {

    private final CustomerSessionService service;

    @PostMapping(path = "/register/new/")
    public void registerNewSession(@RequestBody String id){
        service.registerNewCustomerSession(id);
    }

    @GetMapping(path = "/request/page/{session_id}")
    public int getCurrentPageOfSession(@PathVariable String session_id){
        return service.getCurrentPage(session_id);
    }

    @PutMapping(path = "/change/{session_id}/page/{page}")
    public void setCurrentPage(@PathVariable("session_id") String session_id, @PathVariable("page") String page){
        service.changeCurrentPage(session_id, page);
    }

    @PutMapping(path = "/over/{session_id}")
    public void overTheSession(@PathVariable String session_id){
        this.service.overTheSession(session_id);
    }

    @GetMapping(path = "/request/new")
    public String requestNewSessionID(){
        return service.sendNewSessionID();
    }
}
