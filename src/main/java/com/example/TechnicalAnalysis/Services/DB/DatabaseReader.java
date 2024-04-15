package com.example.TechnicalAnalysis.Services.DB;

import com.example.TechnicalAnalysis.Models.GitHubCollaborator;
import com.example.TechnicalAnalysis.Models.GitHubRepository;
import com.example.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseReader {
    public GitHubCollaborator findCollaborator(CollaboratorRepository repository, String name) {
        return repository.findByName(name);
    }

    public List<GitHubCollaborator> getCollaborators(CollaboratorRepository repository) {
        return repository.findAll();
    }

    public GitHubRepository getProjectRepository(ProjectRepository repository, String name) {
        return repository.findByName(name);
    }
}
