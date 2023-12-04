package com.example.TechnicalAnalysis.Services.GitHubService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.GitHubCollaboratorList;

// Implemented factory pattern along with singleton pattern
public abstract class GitHubCollaboratorBuilder {
    private static final GitHubCollaboratorList collaborators = new GitHubCollaboratorList();

    public static GitHubCollaborator getCollaborator(String name, String email) {
        GitHubCollaborator collaborator = (GitHubCollaborator) collaborators.get(email);
        if (collaborator == null) {
            collaborator = new GitHubCollaborator(name, email);
            collaborators.add(collaborator);
        }
        return collaborator;
    }
}
