package com.Server.TechnicalAnalysis.Controllers;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GroupedCommitsResponse;
import com.Server.TechnicalAnalysis.Repositories.CommitRepository;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/commits")
public class CommitsController {
    @Autowired
    private CommitRepository repository;

    @GetMapping
    public List<GitHubCommit> getAllCommits() {
        return this.repository.findAll();
    }

    @GetMapping("/grouped")
    public List<GroupedCommitsResponse> getGroupedCommits(@Param("projectName") String projectName) {
        Map<String, List<GitHubCommit>> grouped = this.repository
                .findAllByProjectName(projectName)
                .stream().collect(Collectors.groupingBy(GitHubCommit::getDate));
        Set<String> keySet = grouped.keySet();
        List<GroupedCommitsResponse> response = new ArrayList<>();
        for (String key : keySet) {
            GitHubCommitList commits = new GitHubCommitList().addAll(grouped.get(key));
            response.add(new GroupedCommitsResponse(key, commits.findMaxFileTd(), commits));
        }
        return response;
    }

    @GetMapping("/{sha}")
    public GitHubCommit getCommit(@PathVariable String sha) {
        return this.repository.findBySha(sha);
    }
}
