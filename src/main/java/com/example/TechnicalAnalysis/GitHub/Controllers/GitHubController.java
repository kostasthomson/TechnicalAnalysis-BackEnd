package com.example.TechnicalAnalysis.GitHub.Controllers;

import com.example.TechnicalAnalysis.GitHub.Clients.GitHubClient;
import com.example.TechnicalAnalysis.GitHub.Clients.GitHubHttpClient;
import com.example.TechnicalAnalysis.GitHub.Collections.GitHubCollaboratorList;
import com.example.TechnicalAnalysis.GitHub.Collections.GitHubCommitList;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCollaborator;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCommit;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;

public class GitHubController {
    GitHubClient gitHubClient;
    public GitHubController() {
        this.gitHubClient = new GitHubHttpClient();
    }
    public GitHubCollaboratorList HttpCollaboratorsRequest() {
        return (GitHubCollaboratorList) this.gitHubClient.request(GitHubHttpClient.EndPoint.COLLABORATORS);
    }
    public GitHubCommitList HttpCommitsRequest() {
        return (GitHubCommitList) this.gitHubClient.request(GitHubHttpClient.EndPoint.COMMITS);
    }

    public void HttpCommitRequest(GitHubEntity commit) {
        this.gitHubClient.request(GitHubHttpClient.EndPoint.COMMITS, (GitHubCommit) commit);
    }

    //TODO: Find a more efficient solution to map relationships
    public void CreateRelation(GitHubCommitList commits, GitHubCollaboratorList collaborators) {
        for (GitHubEntity commit : commits) {
            GitHubCommit temp_commit = (GitHubCommit) commit;
            for (GitHubEntity collaborator: collaborators) {
                GitHubCollaborator temp_collaborator = (GitHubCollaborator) collaborator;
                if (temp_collaborator.hasId(temp_commit.getAuthorId())) {
                    temp_commit.setAuthor(temp_collaborator);
                }
            }
        }
    }
}
