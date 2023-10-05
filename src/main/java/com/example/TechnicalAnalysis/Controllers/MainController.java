package com.example.TechnicalAnalysis.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String GetRootPath() {
        return "Nothing to see here";
    }
}
