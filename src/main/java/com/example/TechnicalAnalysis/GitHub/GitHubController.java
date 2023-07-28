package com.example.TechnicalAnalysis.GitHub;

import com.example.TechnicalAnalysis.GitHub.RequestMethods.GitHubCLI;
import com.example.TechnicalAnalysis.GitHub.RequestMethods.GitHubHTTP;

public abstract class GitHubController {
    public static void HttpRequestCollaborators() {
        GitHubHTTP.GetCollaborators();
    }

    public static void HttpRequestCommits() {
        GitHubHTTP.GetCommits();
    }

    public static void CliRequestCollaborators() {
        GitHubCLI.GetCollaborators();
    }
    public static void CliRequestCommits() {
       GitHubCLI.GetCommits();
    }

    public static void HttpRequestCommit(String sha) {GitHubHTTP.GetCommit(sha);}
    public static void CliRequestCommit(String sha) {GitHubCLI.GetCommit(sha);}
}
