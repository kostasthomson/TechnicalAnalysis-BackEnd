package com.example.TechnicalAnalysis.Services.ConnectorService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseController;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.GenericRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubEntityCollection;
import com.example.TechnicalAnalysis.Services.GitHubService.Endpoints.EndpointsUtils.MapKeys;
import com.example.TechnicalAnalysis.Services.GitHubService.GitHubController;

import java.util.Map;

public class Analyzer {
    GitHubController gitHubController;
    DatabaseController dbController;

    public Analyzer(Map<MapKeys, GenericRepository<?, ?>> repos) {
        this.gitHubController = new GitHubController();
//        this.dbController = new DatabaseController(repos);
    }

    public void fetch() {
        this.gitHubController.useFetch();
        this.gitHubController.createRelationships();
    }

    public void write() {
        Map<MapKeys, GitHubEntityCollection> results = this.gitHubController.getFetchResults();
//        this.dbController.write(results);
    }
}
