package com.example.TechnicalAnalysis.Services.ConnectorService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseController;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.GenericRepository;
import com.example.TechnicalAnalysis.Services.GitHubService.GitHubController;

public class Analyzer {
    GitHubController gitHubController;
    DatabaseController dbController;

    public Analyzer(GenericRepository<?,?>[] repos) {
        this.gitHubController = new GitHubController();
        this.dbController = new DatabaseController(repos);
    }

    public void start() {
        this.gitHubController.useFetch();
        this.dbController.write();
    }
}
