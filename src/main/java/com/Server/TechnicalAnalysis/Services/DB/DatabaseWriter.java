package com.Server.TechnicalAnalysis.Services.DB;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;
import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubEntity;
import com.Server.TechnicalAnalysis.Models.GitHubRepository;
import com.Server.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.Server.TechnicalAnalysis.Repositories.CommitRepository;
import com.Server.TechnicalAnalysis.Repositories.ProjectRepository;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubEntityCollection;
import org.springframework.stereotype.Service;

@Service
public class DatabaseWriter {
    public void saveCollaborators(CollaboratorRepository repo, GitHubEntityCollection collection) {
        for (GitHubEntity collaborator : collection)
            repo.save((GitHubCollaborator) collaborator);
    }

    public void saveCommits(CommitRepository repo, GitHubCommitList collection) {
        for (GitHubEntity commit : collection)
            repo.save((GitHubCommit) commit);
    }

    public void saveRepository(ProjectRepository repo, GitHubRepository gitHubRepository) {
        repo.save(gitHubRepository);
    }
}
