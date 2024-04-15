package com.example.TechnicalAnalysis.Services.Log;

import com.example.TechnicalAnalysis.Models.GitHubCollaborator;
import com.example.TechnicalAnalysis.Models.GitHubCommit;
import com.example.TechnicalAnalysis.Models.GitHubFile;
import com.example.TechnicalAnalysis.Services.CLI.GitHubCLI;
import com.example.TechnicalAnalysis.Services.DB.DatabaseController;
import com.example.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
import com.example.TechnicalAnalysis.Utils.Lists.GitHubFileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class GitLogInterpreter {
    private final Logger logger = LoggerFactory.getLogger(GitLogInterpreter.class);

    @Autowired
    private GitHubCLI gitHubCLI;
    @Autowired
    private DatabaseController dbController;

    private GitHubCommit createCommit(List<String> commitsList) {
        List<String> commitInfo = Arrays.stream(commitsList.get(0).split(","))
                .map(String::trim)
                .toList();
        List<String> commitFiles = commitsList.subList(1, commitsList.size());
        List<GitHubFile> files = GitHubFileList.parseToList(commitFiles);
        if (files.isEmpty()) return null;
        String sha = commitInfo.get(0);
        String message = commitInfo.get(5);
        String date = OffsetDateTime.parse(
                commitInfo.get(4),
                DateTimeFormatter.ofPattern(
                        "E MMM d HH:mm:ss yyyy Z",
                        Locale.ENGLISH
                )
        ).format(DateTimeFormatter.ISO_DATE_TIME);
        GitHubCollaborator collaborator = this.dbController.findCollaborator(commitInfo.get(1));
        List<String> tags = this.gitHubCLI.getPullRequestTags(sha);
        return new GitHubCommit(sha, message, date, collaborator, files, tags);
    }

    public GitHubCommitList createCommitsList(List<List<String>> logCommits) {
        try {
            GitHubCommitList commitList = new GitHubCommitList();
            commitList.addAll(
                    logCommits.stream()
                            .map(this::createCommit)
                            .filter(Objects::nonNull)
                            .toList()
            );
            int failed = this.gitHubCLI.getFailed();
            int succeed = this.gitHubCLI.getSucceed();
            if (failed != 0) logger.warn("getPullRequestTags: Failed {}", failed);
            if (succeed != 0) logger.warn("getPullRequestTags: Succeed {}", succeed);
            return commitList;
        } catch (NullPointerException npe) {
            logger.error("Cannot interpret logged commits: {}", npe.getMessage());
        }
        return null;
    }
}
