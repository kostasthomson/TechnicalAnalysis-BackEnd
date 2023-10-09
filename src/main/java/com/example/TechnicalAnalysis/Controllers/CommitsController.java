package com.example.TechnicalAnalysis.Controllers;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commits")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public class CommitsController {

    private final CommitRepository repository;

    @Autowired
    public CommitsController(CommitRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<GitHubCommit> GetAllCommits() {
        return (List<GitHubCommit>) this.repository.findAll();
    }

    @GetMapping("/{sha}")
    public GitHubCommit GetCommit(@PathVariable String sha) {
        return this.repository.findBySha(sha);
    }
}
