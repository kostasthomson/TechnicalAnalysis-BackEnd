package com.example.TechnicalAnalysis.GitHub;

import com.example.TechnicalAnalysis.GitHub.Controllers.GitHubController;
import com.example.TechnicalAnalysis.GitHub.Entities.Collections.GitHubCollaboratorList;
import com.example.TechnicalAnalysis.GitHub.Entities.Collections.GitHubCommitList;

public class GitHubApplication {
    public static void main(String[] args) {
        GitHubController controller = new GitHubController();
        //collaborators request
        //http request
        GitHubCollaboratorList collaborators = controller.HttpCollaboratorsRequest();
        //commits request
        //http request
        GitHubCommitList commits = controller.HttpCommitsRequest();
        //for each of commits, find commit info
        commits.forEach(controller::HttpCommitRequest);
        //print ok
        System.out.println("ok");
        collaborators.printList();
        commits.printList();
    }
}
