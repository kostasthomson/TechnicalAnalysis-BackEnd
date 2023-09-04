package com.example.TechnicalAnalysis.Services.ConnectorService;

import com.example.TechnicalAnalysis.Services.ClientService.GitHubClient;
import com.example.TechnicalAnalysis.Services.ClientService.GitHubHttpClient;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubEntity;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.GenericRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubCollaboratorList;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubCommitList;

//TODO: create custom response for each endpoint to match frontend needs
public class ConnectorController {
    Analyzer analyzer;
    Retriever retriever = new Retriever();
    GitHubClient gitHubClient;
    public ConnectorController(GenericRepository<?, ?>[] repos) {
        this.analyzer = new Analyzer(repos);
        this.analyzer.start();
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
