package com.example.TechnicalAnalysis.Services.DatabaseService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubEntity;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubEntityCollection;

public class DatabaseWriter {
    public void saveCollaborators(CollaboratorRepository repo, GitHubEntityCollection collection) {
        for (GitHubEntity collaborator : collection)
            repo.save((GitHubCollaborator) collaborator);
    }

    public void saveCommits(CommitRepository repo, GitHubEntityCollection collection) {
        for (GitHubEntity commit : collection)
            repo.save((GitHubCommit) commit);
    }
}
