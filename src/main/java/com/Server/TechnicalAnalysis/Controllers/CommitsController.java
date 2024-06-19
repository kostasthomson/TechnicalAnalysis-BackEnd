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
import java.util.Collections;
import java.util.List;

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
        List<GitHubCommit> grouped = this.repository.findAllByProjectName(projectName);
        Collections.sort(grouped);
        List<GroupedCommitsResponse> response = new ArrayList<>();
        int firstIndex = 0;
        for (int i = 0; i < grouped.size(); i++) {
            GitHubCommit currentCommit = grouped.get(i);
            if (currentCommit.isWeekCommit()) {
                response.add(new GroupedCommitsResponse(currentCommit.getDate(), new GitHubCommitList().addAll(grouped.subList(firstIndex, i + 1))));
                firstIndex = i + 1;
            }
        }
        return response;
    }

    @GetMapping("/{sha}")
    public GitHubCommit getCommit(@PathVariable String sha) {
        return this.repository.findBySha(sha);
    }
}
