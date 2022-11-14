package com.shop4me.core.adapter.inbound.user;

import com.shop4me.core.domain.port.requesting.CustomerRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@CrossOrigin

@Async
@RestController

@AllArgsConstructor

@RequestMapping("/customer")
public class CustomerServiceRestController {

    private final CustomerRequestRepository customerRequestRepository;

}
