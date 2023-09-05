package com.example.TechnicalAnalysis.Services.DatabaseService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.GenericRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubEntityCollection;
import com.example.TechnicalAnalysis.Services.GitHubService.Endpoints.EndpointsUtils.MapKeys;

import java.util.Map;

public class DatabaseController {
    private final DatabaseWriter dbWriter = new DatabaseWriter();
    private final DatabaseReader dbReader = new DatabaseReader();

    private final Map<MapKeys, GenericRepository<?, ?>> repositories;

    public DatabaseController(Map<MapKeys, GenericRepository<?, ?>> repos) {
        this.repositories = repos;
    }

    public void write(Map<MapKeys, GitHubEntityCollection> map) {
        MapKeys key;
        key = MapKeys.COLLABORATORS;
        this.dbWriter.saveCollaborators((CollaboratorRepository) this.repositories.get(key), map.get(key));
        key = MapKeys.COMMITS;
        this.dbWriter.saveCommits((CommitRepository) this.repositories.get(key), map.get(key));
    }

    public void read() {
        this.dbReader.get();
    }
}
