package com.example.TechnicalAnalysis.Services.ConnectorService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.GenericRepository;
import com.example.TechnicalAnalysis.Services.GitHubService.Endpoints.EndpointsUtils.MapKeys;

import java.util.Map;

//TODO: create custom response for each endpoint to match frontend needs
//TODO: REST CONTROLLER (GITHUB URL {PARAM})
public class ConnectorController {
    Analyzer analyzer;
    Retriever retriever = new Retriever();

    public ConnectorController(Map<MapKeys, GenericRepository<?, ?>> repos) {
        this.analyzer = new Analyzer(repos);
    }

    public void startAnalyzer() {
        this.analyzer.fetch();
        this.analyzer.write();
    }
}
