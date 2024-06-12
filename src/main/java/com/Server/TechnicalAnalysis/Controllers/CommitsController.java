package com.Server.TechnicalAnalysis.Controllers;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GroupedCommitsResponse;
import com.Server.TechnicalAnalysis.Repositories.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        return this.repository.findAll();
    }

    @GetMapping("/grouped")
    public List<GroupedCommitsResponse> GetGroupedCommits() {
        List<GitHubCommit> commits = this.repository.findAll();
        Map<String, List<GitHubCommit>> grouped =  commits.stream().collect(Collectors.groupingBy(GitHubCommit::getDate));
        Set<String> keySet = grouped.keySet();
        List<GroupedCommitsResponse> response = new ArrayList<>();
        for (String key : keySet) {
            response.add(new GroupedCommitsResponse(key, grouped.get(key)));
        }
        return response;
    }

    @GetMapping("/{sha}")
    public GitHubCommit GetCommit(@PathVariable String sha) {
        return this.repository.findBySha(sha);
    }
}
