package com.Server.TechnicalAnalysis.Controllers;


import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    private FileRepository repository;

    @GetMapping
    public List<GitHubFile> getAllFiles() {
        return this.repository.findAll();
    }

    @GetMapping("/{name}")
    public List<GitHubFile> getFile(@PathVariable String name) {
        return this.repository.findByName(name);
    }
}
