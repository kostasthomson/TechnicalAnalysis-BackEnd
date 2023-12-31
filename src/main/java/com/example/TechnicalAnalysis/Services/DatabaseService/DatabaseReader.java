package com.example.TechnicalAnalysis.Services.DatabaseService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;

public class DatabaseReader {
    public GitHubCollaborator findCollaborator(CollaboratorRepository repository, String name) {
        return repository.findByName(name);
    }
}
