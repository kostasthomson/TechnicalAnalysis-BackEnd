package com.Server.TechnicalAnalysis.Services.Log;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;
import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Services.CLI.GitHubCLI;
import com.Server.TechnicalAnalysis.Services.DB.DatabaseController;
import com.Server.TechnicalAnalysis.Utils.GitHubCollaboratorBuilder;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubFileList;
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
        List<String> commitFiles = commitsList.subList(1, commitsList.size());
        List<GitHubFile> files = GitHubFileList.parseToList(commitFiles);
        if (files.isEmpty()) return null;
        List<String> commitInfo = Arrays.stream(commitsList.get(0).split(","))
                .map(String::trim)
                .toList();
        String sha = commitInfo.get(0);
        String date = OffsetDateTime.parse(
                commitInfo.get(4),
                DateTimeFormatter.ofPattern(
                        "E MMM d HH:mm:ss yyyy Z",
                        Locale.ENGLISH
                )
        ).format(DateTimeFormatter.ISO_DATE_TIME);
        String message = commitInfo.get(5);
        // todo: duplicate contributors
        GitHubCollaborator collaborator = GitHubCollaboratorBuilder.getCollaborator(commitInfo.get(3).toLowerCase());
        return new GitHubCommit(sha, message, date, collaborator, files);
    }

    public GitHubCommitList createCommitsList(List<List<String>> logCommits) {
        try {
            GitHubCommitList commitList = new GitHubCommitList();
            commitList.addAll(
                    logCommits.stream()
                            .map(this::createCommit)
                            .filter(Objects::nonNull)
                            .sorted()
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

    public GitHubCollaboratorList createCollaboratorList(List<String[]> logAuthors) {
        try {
            logAuthors.stream().map(GitHubCollaboratorBuilder::createCollaborator);
            return GitHubCollaboratorBuilder.getList();
        } catch (NullPointerException npe) {
            logger.error("Cannot interpret logged authors: {}", npe.getMessage());
            return null;
        }
    }
}