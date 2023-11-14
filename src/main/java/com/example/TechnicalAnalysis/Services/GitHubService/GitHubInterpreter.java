package com.example.TechnicalAnalysis.Services.GitHubService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.GitHubCommitList;

import java.util.Arrays;
import java.util.List;

public abstract class GitHubInterpreter {
    private static final GitHubCommitList commitList = new GitHubCommitList();

    private static GitHubCommit createCommit(List<String> commitsList) {
        List<String> commitInfo = Arrays.stream(commitsList.get(0).split(","))
                .map(String::trim)
                .toList();
        List<String> commitFiles = commitsList.subList(1, commitsList.size());
        return new GitHubCommit(commitInfo, commitFiles);
    }

    public static void CreateCommitsList() {
        List<List<String>> loggedCommits = GitHubLogReader.getLogCommits();
        try {
            commitList.addAll(
                    loggedCommits.stream()
                            .map(GitHubInterpreter::createCommit)
                            .toList()
            );
        } catch (NullPointerException npe) {
            System.out.println("Cannot interpret logged commits: " + npe.getMessage());
        }
    }

    public static GitHubCommitList getCommitsList() {
        return commitList;
    }
}
