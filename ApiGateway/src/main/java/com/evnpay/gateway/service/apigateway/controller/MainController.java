package com.evnpay.gateway.service.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/actuator/info")
    String info(){
        return "This is Gateway Side";
    }
}
