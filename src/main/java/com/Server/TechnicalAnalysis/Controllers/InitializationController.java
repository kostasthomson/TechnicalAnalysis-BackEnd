package com.Server.TechnicalAnalysis.Controllers;

import com.Server.TechnicalAnalysis.Services.InitializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/init")
public class InitializationController {
    @Autowired
    private InitializationService initializationService;

    @GetMapping
    public void InitializeApplication(@RequestParam String link) {
        this.initializationService.startInitialization(link);
    }
}
