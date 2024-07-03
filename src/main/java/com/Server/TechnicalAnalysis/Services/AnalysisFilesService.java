package com.Server.TechnicalAnalysis.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class AnalysisFilesService {
    private final Logger logger = LoggerFactory.getLogger(AnalysisFilesService.class);

    public ResponseEntity<ByteArrayResource> exportFile(String fileName) {
        File file = new File(fileName + ".csv");
        try (FileInputStream inputStream = new FileInputStream(file)){
            ByteArrayResource resource = new ByteArrayResource(inputStream.readAllBytes());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + resource.getFilename())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IOException e) {
            this.logger.error("exportFile: {}", e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }
}
