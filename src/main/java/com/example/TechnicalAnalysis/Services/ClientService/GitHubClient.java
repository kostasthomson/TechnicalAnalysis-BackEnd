package com.example.TechnicalAnalysis.Services.ClientService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubEntityCollection;

public abstract class GitHubClient {
    public abstract GitHubEntityCollection request(GitHubHttpClient.EndPoint endPoint);
    public abstract void request(GitHubHttpClient.EndPoint endPoint, GitHubCommit commit);
}
