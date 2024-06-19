package com.Server.TechnicalAnalysis.Services.Log;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;
import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubFileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final GitHubCommitList commitList = new GitHubCommitList();
    private final GitHubCollaboratorList collaboratorList = new GitHubCollaboratorList();

    private GitHubCommit createCommit(List<String> commitsList, String projectId) {
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
        String authorKey = commitInfo.get(2).trim().split(" ")[0];
        GitHubCollaborator collaborator = collaboratorList.get(authorKey);
        if (collaborator == null) this.logger.warn("No author found: Commit [{}]", sha);
        return new GitHubCommit(projectId, sha, message, date, collaborator, files);
    }

    public GitHubCommitList createCommitsList(List<List<String>> logCommits, String projectId) {
        try {
            return commitList.addAll(
                    logCommits.stream()
                            .map(sList -> this.createCommit(sList, projectId))
                            .filter(Objects::nonNull)
                            .sorted()
                            .toList()
            );
        } catch (NullPointerException npe) {
            logger.error("Cannot interpret logged commits: {}", npe.getMessage());
        }
        return null;
    }

    private void createCollaborator(String[] info) {
        // check if collaborator already exists
        if (collaboratorList.get(info[1]) != null) return; // key = email
        GitHubCollaborator collaborator = new GitHubCollaborator(info[0], info[1]);
        collaboratorList.add(collaborator);
    }

    public GitHubCollaboratorList createCollaboratorList(List<String[]> logAuthors) {
        try {
            logAuthors.forEach(this::createCollaborator);
            return this.collaboratorList;
        } catch (NullPointerException npe) {
            logger.error("Cannot interpret logged authors: {}", npe.getMessage());
        }
        return null;
    }

    public void reset() {
        this.collaboratorList.clear();
        this.commitList.clear();
    }
}