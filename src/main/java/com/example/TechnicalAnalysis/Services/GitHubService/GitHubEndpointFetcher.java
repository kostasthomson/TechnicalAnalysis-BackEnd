package com.example.TechnicalAnalysis.Services.GitHubService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubEntityCollection;
import com.example.TechnicalAnalysis.Services.GitHubService.Endpoints.EndpointsUtils.MapKeys;
import com.example.TechnicalAnalysis.Services.GitHubService.Endpoints.GitHubCollaboratorsEndPoint;
import com.example.TechnicalAnalysis.Services.GitHubService.Endpoints.GitHubCommitsEndPoint;

import java.util.HashMap;

public class GitHubEndpointFetcher {
    private final GitHubCommitsEndPoint commitsEndPoint;
    private final GitHubCollaboratorsEndPoint collaboratorsEndPoint;

    private final HashMap<MapKeys, GitHubEntityCollection> fetchResult;

    public GitHubEndpointFetcher() {
        this.commitsEndPoint = new GitHubCommitsEndPoint();
        this.collaboratorsEndPoint = new GitHubCollaboratorsEndPoint();
        this.fetchResult = new HashMap<>();
    }

    public void fetchCommits() {
        this.fetchResult.put(MapKeys.COMMITS, this.commitsEndPoint.request());
    }

    public void fetchCollaborators() {
        this.fetchResult.put(MapKeys.COLLABORATORS, this.collaboratorsEndPoint.request());
    }

    public HashMap<MapKeys, GitHubEntityCollection> getFetchResult() {
        return this.fetchResult;
    }
}
