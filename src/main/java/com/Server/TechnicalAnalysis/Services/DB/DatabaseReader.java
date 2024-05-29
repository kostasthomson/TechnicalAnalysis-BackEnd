package com.Server.TechnicalAnalysis.Services.DB;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;
import com.Server.TechnicalAnalysis.Models.GitHubRepository;
import com.Server.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.Server.TechnicalAnalysis.Repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseReader {
    public GitHubCollaborator findCollaborator(CollaboratorRepository repository, String key) {
        return repository.findByEmail(key);
    }

    public List<GitHubCollaborator> getCollaborators(CollaboratorRepository repository) {
        return repository.findAll();
    }

    public GitHubRepository getProjectRepository(ProjectRepository repository, String name) {
        return repository.findByName(name);
    }
}
