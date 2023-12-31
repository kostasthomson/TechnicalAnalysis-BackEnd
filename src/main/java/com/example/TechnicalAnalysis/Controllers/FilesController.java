package com.example.TechnicalAnalysis.Controllers;


import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubFile;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.FileRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public class FilesController {

    private final FileRepository repository;

    public FilesController(FileRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<GitHubFile> GetAllFiles() {
        return (List<GitHubFile>) this.repository.findAll();
    }

    @GetMapping("/{name}")
    public GitHubFile GetFile(@PathVariable String name) {
        return this.repository.findByName(name);
    }

}
