package com.Server.TechnicalAnalysis.Services.DB;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;
import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubRepository;
import com.Server.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.Server.TechnicalAnalysis.Repositories.CommitRepository;
import com.Server.TechnicalAnalysis.Repositories.ProjectRepository;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
import org.springframework.stereotype.Service;

@Service
public class DatabaseWriter {
    public void saveCollaborators(CollaboratorRepository repo, GitHubCollaboratorList collection) {
        for (GitHubCollaborator collaborator : collection)
            repo.save(collaborator);
    }

    public void saveCommits(CommitRepository repo, GitHubCommitList collection) {
        for (GitHubCommit commit : collection)
            repo.save(commit);
    }

    public void saveRepository(ProjectRepository repo, GitHubRepository gitHubRepository) {
        repo.save(gitHubRepository);
    }
}
