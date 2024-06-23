package com.Server.TechnicalAnalysis.Controllers;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GroupedCommitsResponse;
import com.Server.TechnicalAnalysis.Services.CommitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commits")
public class CommitsController {
    @Autowired
    private CommitsService service;

    @GetMapping
    public List<GitHubCommit> getAllCommits() {
        return this.service.getAllCommits();
    }

    @GetMapping("/grouped")
    public List<GroupedCommitsResponse> getGroupedCommits(@RequestParam String projectName) {
        return this.service.getGroupedCommits(projectName);
    }

    @GetMapping("/{sha}")
    public GitHubCommit getCommit(@PathVariable String sha) {
        return this.service.getCommit(sha);
    }
}
