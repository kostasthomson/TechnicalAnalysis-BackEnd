package com.example.TechnicalAnalysis.GitHub;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import com.example.TechnicalAnalysis.GitHub.RequestMethods.GitHubHTTP;

import java.util.List;

public class GitHubApplication {

    public static void main(String[] args) {
        GitHubController.CliRequestCommits();
        List<GitHubEntity> commits = GitHubHTTP.commits.getList();
//        for (GitHubEntity e : commits) {
//            GitHubController.HttpRequestCommit(e.toString());
//
//            ((GitHubCommitsEndPoint) GitHubHTTP.commits).UpdateCommit((GitHubCommit) e, GitHubHTTP.commit.getCommit());
//        }
    }
}
