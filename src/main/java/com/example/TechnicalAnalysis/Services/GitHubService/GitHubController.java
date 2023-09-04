package com.example.TechnicalAnalysis.Services.GitHubService;

public class GitHubController {

    private final GitHubEndpointFetcher fetcher;
    private final ActionsListener listener = null;

    public GitHubController() {
        this.fetcher = new GitHubEndpointFetcher();
    }

    public void useFetch() {
        this.fetcher.fetchCommits();
        this.fetcher.fetchCollaborators();
    }

}
