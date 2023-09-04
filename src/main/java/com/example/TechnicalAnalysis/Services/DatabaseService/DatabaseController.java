package com.example.TechnicalAnalysis.Services.DatabaseService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.GenericRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubEntityCollection;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.InputOutput.DatabaseReader;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.InputOutput.DatabaseWriter;

public class DatabaseController {
    private final DatabaseWriter dbWriter = new DatabaseWriter();
    private final DatabaseReader dbReader = new DatabaseReader();

    private final GenericRepository<?,?>[] repositories;

    public DatabaseController(GenericRepository<?,?>[] repos) {
        this.repositories = repos;
    }

    public void write(GitHubEntityCollection collection) {
        try {
            this.dbWriter.saveCollaborators((CollaboratorRepository) this.repositories[0], collection);
        } catch (Exception ignored) {
            this.dbWriter.saveCollaborators((CollaboratorRepository) this.repositories[1], collection);
        }
    }

    public void read() {
        this.dbReader.get();
    }
}
