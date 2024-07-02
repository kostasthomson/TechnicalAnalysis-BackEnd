package com.Server.TechnicalAnalysis.Controllers;

import com.Server.TechnicalAnalysis.Services.AnalysisFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
public class AnalysisFilesController {
    @Autowired
    AnalysisFilesService service;

    @GetMapping
    public ResponseEntity<ByteArrayResource> exportFile(@RequestParam(value = "fileName") String fileName) {
       return this.service.exportFile(fileName);
    }
}
