package com.Server.TechnicalAnalysis.Services;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GroupedCommitsResponse;
import com.Server.TechnicalAnalysis.Repositories.CommitRepository;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CommitsService {
    private final Logger logger = LoggerFactory.getLogger(CommitsService.class);

    @Autowired
    private CommitRepository repository;

    public List<GitHubCommit> getAllCommits() {
        return this.repository.findAll();
    }

    public List<GroupedCommitsResponse> getGroupedCommits(String projectName) {
        List<GitHubCommit> storedCommits = this.repository.findAllByProjectName(projectName);
        Collections.sort(storedCommits);
        List<GroupedCommitsResponse> response = new ArrayList<>();
        int firstIndex = 0;
        for (int i = 0; i < storedCommits.size(); i++) {
            GitHubCommit currentCommit = storedCommits.get(i);
            if (!currentCommit.isWeekCommit()) continue;
            response.add(new GroupedCommitsResponse(currentCommit.getDate(), new GitHubCommitList().addAll(storedCommits.subList(firstIndex, i + 1))));
            firstIndex = i + 1;
        }
        return response;
    }

    public GitHubCommit getCommit(String sha) {
        return this.repository.findBySha(sha);
    }

}
