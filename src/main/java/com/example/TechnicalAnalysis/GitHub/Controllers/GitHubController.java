package com.example.TechnicalAnalysis.GitHub.Controllers;

import com.example.TechnicalAnalysis.GitHub.Clients.GitHubClient;
import com.example.TechnicalAnalysis.GitHub.Clients.GitHubHttpClient;
import com.example.TechnicalAnalysis.GitHub.Entities.Collections.GitHubCollaboratorList;
import com.example.TechnicalAnalysis.GitHub.Entities.Collections.GitHubCommitList;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCommit;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;

public class GitHubController {
    GitHubClient gitHubClient;
    public GitHubController() {
        this.gitHubClient = new GitHubHttpClient();
    }
    public GitHubCollaboratorList HttpCollaboratorsRequest() {
        return (GitHubCollaboratorList) this.gitHubClient.request(GitHubHttpClient.EndPoint.COLLABORATORS);
    }
    public GitHubCommitList HttpCommitsRequest() {
        return (GitHubCommitList) this.gitHubClient.request(GitHubHttpClient.EndPoint.COMMITS);
    }

    public void HttpCommitRequest(GitHubEntity commit) {
        this.gitHubClient.request(GitHubHttpClient.EndPoint.COMMITS, (GitHubCommit) commit);
    }
}
