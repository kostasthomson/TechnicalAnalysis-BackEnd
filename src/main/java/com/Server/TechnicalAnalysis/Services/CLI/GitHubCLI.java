package com.Server.TechnicalAnalysis.Services.CLI;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.List;

@Service
public class GitHubCLI extends SimpleCLI{
    private final Logger logger = LoggerFactory.getLogger(GitHubCLI.class);
    private final String GITHUB_COMMAND = "gh";
    private int succeed = 0;
    private int failed = 0;

    public void addPullRequestTags(GitHubCommit commit) {
        // Start the process
        try {
            BufferedReader bufferedReader = this.runCommand(String.format(GITHUB_COMMAND + " pr list --search \"%s\" --state merged --json labels --jq .[0].labels[].name", commit.getSha()));
            List<String> lines = bufferedReader.lines().toList();
            if (lines.contains("[]")) throw new EmptyTagListException();
            List<String> tags = lines.stream().map(str->str.replace("\n", "")).filter(str->!str.isEmpty()).toList();
            commit.setTags(tags);
            succeed++;
        } catch (EmptyTagListException e) {
            failed++;
        }
    }

    public int getSucceed() {
        return succeed;
    }

    public int getFailed() {
        return failed;
    }
}
