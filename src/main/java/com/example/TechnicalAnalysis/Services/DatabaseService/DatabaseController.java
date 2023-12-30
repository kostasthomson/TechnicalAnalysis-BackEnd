package com.example.TechnicalAnalysis.Services.DatabaseService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.GitHubCollaboratorList;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.GitHubCommitList;

public abstract class DatabaseController {
    private static final DatabaseWriter dbWriter = new DatabaseWriter();
    private static final DatabaseReader dbReader = new DatabaseReader();
    private static CommitRepository commitRepository;
    private static CollaboratorRepository collaboratorRepository;

    public static void addCommitRepository(CommitRepository repo) {
        commitRepository = repo;
    }

    public static void addCollaboratorRepository(CollaboratorRepository repo) {
        collaboratorRepository = repo;
    }

    public static void WriteCommits(GitHubCommitList list) {
        dbWriter.saveCommits(commitRepository, list);
    }


    public static void WriteCollaborators(GitHubCollaboratorList list) {
        dbWriter.saveCollaborators(collaboratorRepository, list);
    }

    public static GitHubCollaborator findCollaborator(String name) {
        return dbReader.findCollaborator(collaboratorRepository, name.replace(" ", ""));
    }
}
