package com.Server.TechnicalAnalysis.Controllers;


import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FilesController {

    @Autowired
    private FileService service;

    @GetMapping
    public List<GitHubFile> getAllFiles() {
        return this.service.getAllFiles();
    }

    @GetMapping("/{name}")
    public List<GitHubFile> getFile(@PathVariable String name) {
        return this.service.getFile(name);
    }
}
