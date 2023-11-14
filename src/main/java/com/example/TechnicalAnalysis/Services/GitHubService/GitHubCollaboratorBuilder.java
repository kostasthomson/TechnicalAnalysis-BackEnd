package com.example.TechnicalAnalysis.Services.GitHubService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.GitHubCollaboratorList;

// Implemented factory pattern along with singleton pattern
public abstract class GitHubCollaboratorBuilder {
    private static final GitHubCollaboratorList collaborators = new GitHubCollaboratorList();

    public static GitHubCollaborator getCollaborator(String id, String name) {
        GitHubCollaborator collaborator = (GitHubCollaborator) collaborators.get(id);
        if (collaborator == null) {
            collaborator = new GitHubCollaborator(id, name);
            collaborators.add(collaborator);
        }
        return collaborator;
    }
}
