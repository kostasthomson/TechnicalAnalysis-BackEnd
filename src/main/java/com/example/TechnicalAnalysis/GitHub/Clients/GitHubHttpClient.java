package com.example.TechnicalAnalysis.GitHub.Clients;

import com.example.TechnicalAnalysis.GitHub.EndPoints.GitHubCollaboratorsEndPoint;
import com.example.TechnicalAnalysis.GitHub.EndPoints.GitHubCommitsEndPoint;
import com.example.TechnicalAnalysis.GitHub.EndPoints.GitHubEndPoint;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCommit;
import com.example.TechnicalAnalysis.GitHub.Entities.Collections.GitHubEntityCollection;

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
