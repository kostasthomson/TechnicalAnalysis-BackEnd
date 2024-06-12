package com.Server.TechnicalAnalysis.Controllers;


import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Repositories.FileRepository;
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
