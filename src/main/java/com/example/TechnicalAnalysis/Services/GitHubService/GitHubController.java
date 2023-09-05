package com.example.TechnicalAnalysis.Services.GitHubService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubEntity;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubCollaboratorList;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubCommitList;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubEntityCollection;
import com.example.TechnicalAnalysis.Services.GitHubService.Endpoints.EndpointsUtils.MapKeys;

import java.util.Map;

public class GitHubController {

    private final GitHubEndpointFetcher fetcher;
    private final ActionsListener listener = null;

    public GitHubController() {
        this.fetcher = new GitHubEndpointFetcher();
    }

    public void useFetch() {
        this.fetcher.fetchCollaborators();
        this.fetcher.fetchCommits();
    }

    public void createRelationships() {
        GitHubCommitList commits = (GitHubCommitList) this.fetcher.getFetchResult().get(MapKeys.COMMITS);
        GitHubCollaboratorList collaborators = (GitHubCollaboratorList) this.fetcher.getFetchResult().get(MapKeys.COLLABORATORS);
        for (GitHubEntity entity : commits) {
            GitHubCommit commit = (GitHubCommit) entity;
            commit.setAuthor((GitHubCollaborator) collaborators.get(commit.getAuthorId()));
        }
    }

    public Map<MapKeys, GitHubEntityCollection> getFetchResults() {
        return this.fetcher.getFetchResult();
    }

}
