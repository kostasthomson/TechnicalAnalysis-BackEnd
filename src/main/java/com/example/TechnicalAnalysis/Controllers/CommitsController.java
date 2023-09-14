package com.example.TechnicalAnalysis.Controllers;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommitsController {

    private final CommitRepository repository;

    public CommitsController(CommitRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/commits")
    public List<GitHubCommit> GetAllCommits() {
        return (List<GitHubCommit>) this.repository.findAll();
    }

    @GetMapping("/commits/{sha}")
    public GitHubCommit GetCommit(@PathVariable String sha) {
        return this.repository.findBySha(sha);
    }
}
