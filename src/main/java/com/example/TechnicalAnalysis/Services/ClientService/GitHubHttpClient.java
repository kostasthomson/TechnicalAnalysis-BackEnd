package com.example.TechnicalAnalysis.Services.ClientService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubEntityCollection;
import com.example.TechnicalAnalysis.Services.GitHubService.GitHubCollaboratorsEndPoint;
import com.example.TechnicalAnalysis.Services.GitHubService.GitHubCommitsEndPoint;
import com.example.TechnicalAnalysis.Services.GitHubService.GitHubEndPoint;

import java.util.Hashtable;

public class GitHubHttpClient extends GitHubClient {
    public enum EndPoint {
        COLLABORATORS,
        COMMITS
    }
    private final Hashtable<EndPoint, GitHubEndPoint> endpointsTable = new Hashtable<>();

    public GitHubHttpClient() {
        endpointsTable.put(EndPoint.COLLABORATORS, new GitHubCollaboratorsEndPoint());
        endpointsTable.put(EndPoint.COMMITS, new GitHubCommitsEndPoint());
    }
    @Override
    public GitHubEntityCollection request(EndPoint endPoint) {
        return endpointsTable.get(endPoint).request();
    }

    @Override
    public void request(EndPoint endPoint, GitHubCommit commit) {endpointsTable.get(endPoint).request(commit);}
}
