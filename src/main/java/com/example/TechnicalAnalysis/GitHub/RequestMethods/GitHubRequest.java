package com.example.TechnicalAnalysis.GitHub.RequestMethods;

import com.example.TechnicalAnalysis.GitHub.EndPoints.GitHubCollaboratorsEndPoint;
import com.example.TechnicalAnalysis.GitHub.EndPoints.GitHubCommitEndPoint;
import com.example.TechnicalAnalysis.GitHub.EndPoints.GitHubCommitsEndPoint;
import com.example.TechnicalAnalysis.GitHub.EndPoints.GitHubEndPoint;

import java.net.http.HttpClient;

public interface GitHubRequest {
    String owner = "kostasthomson";
    String repo = "BlackJack";
    String base_url = "https://api.github.com/repos/"+owner+"/"+repo+"/";
    String[] headers = new String[] {
            "Accept", "application/vnd.github+json",
            "Authorization", "Bearer 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",
            "X-GitHub-Api-Version", "2022-11-28"
    };
    HttpClient client = HttpClient.newHttpClient();
    GitHubEndPoint collaborators = new GitHubCollaboratorsEndPoint();
    GitHubEndPoint commits = new GitHubCommitsEndPoint();
    GitHubCommitEndPoint commit = new GitHubCommitEndPoint();
    static void GetCollaborators() {}

    static void GetCommits() {}

    static void GetCommit(String ghc_sha) {}
}
