package com.example.TechnicalAnalysis.GitHub.Clients;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCommit;
import com.example.TechnicalAnalysis.GitHub.Entities.Collections.GitHubEntityCollection;

public abstract class GitHubClient {
    public abstract GitHubEntityCollection request(GitHubHttpClient.EndPoint endPoint);
    public abstract void request(GitHubHttpClient.EndPoint endPoint, GitHubCommit commit);
}
