package com.example.TechnicalAnalysis.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitialController {

    @GetMapping("/")
    public String sayHello() {
        return "Hello";
    }
}
