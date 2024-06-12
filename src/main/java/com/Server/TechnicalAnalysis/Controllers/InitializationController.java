package com.Server.TechnicalAnalysis.Controllers;

import com.Server.TechnicalAnalysis.Services.REST.InitializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/init")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public class InitializationController {
    private final Logger logger = LoggerFactory.getLogger(InitializationController.class);
    @Autowired
    private InitializationService initializationService;

    @GetMapping
    public void InitializeApplication(@RequestParam String link) {
        Instant start = Instant.now();
        this.initializationService.startInitialization(link);
        Instant end = Instant.now();
        Duration elapsedTime = Duration.between(start, end);
        long fullSeconds = elapsedTime.getSeconds();
        long hours = fullSeconds / 3600;
        long minutes = fullSeconds % 3600 / 60;
        long seconds = fullSeconds % 60;
        this.logger.info("Execution time -> {}:{}:{}", hours, minutes, seconds);
        this.logger.info("Set up complete {}", link);
        try {
            File file = new File("./RESULTS_LOG.log");
            FileWriter writer = new FileWriter(file, true);
            // header: timestamp, projectId, commits, timeElapsed
            writer.append(String.format("%s,%s,%s,%s:%s:%s",
                    new Timestamp(System.currentTimeMillis()),
                    this.initializationService.getPROJECT_ID(),
                    this.initializationService.getCOMMITS_COUNT(),
                    hours,
                    minutes,
                    seconds
            )).append("\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
