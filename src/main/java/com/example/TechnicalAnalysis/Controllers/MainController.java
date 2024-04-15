package com.example.TechnicalAnalysis.Controllers;

import com.example.TechnicalAnalysis.Services.REST.InitializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/init")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public class MainController {
    private final Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private InitializationService initializationService;

    @GetMapping
    public void InitializeApplication(@RequestParam String link) {
        this.initializationService.startInitialization(link);
        this.logger.info("Set up complete {}", link);
    }
}
