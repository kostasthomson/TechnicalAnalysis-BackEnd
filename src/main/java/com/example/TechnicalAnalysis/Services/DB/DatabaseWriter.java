package com.example.TechnicalAnalysis.Services.DB;

import com.example.TechnicalAnalysis.Models.GitHubCollaborator;
import com.example.TechnicalAnalysis.Models.GitHubCommit;
import com.example.TechnicalAnalysis.Models.GitHubEntity;
import com.example.TechnicalAnalysis.Models.GitHubRepository;
import com.example.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Repositories.ProjectRepository;
import com.example.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
import com.example.TechnicalAnalysis.Utils.Lists.GitHubEntityCollection;
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
