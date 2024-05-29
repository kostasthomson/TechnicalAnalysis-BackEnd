package com.Server.TechnicalAnalysis.Services.CLI;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.TechnicalAnalysisApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class GitHubCLI extends SimpleCLI{
    private final Logger logger = LoggerFactory.getLogger(GitHubCLI.class);
    private int succeed = 0;
    private int failed = 0;

    public void addPullRequestTags(GitHubCommit commit) {
        // Start the process
        try {
            String ghCommand = String.format("gh pr list --search \"%s\" --state merged --json labels --jq .[0].labels[].name", commit.getSha());
            int code = this.ExecuteCommand(ghCommand);
            if (code != 0) throw new CommandExecutionFailedException(code);
            String output = new String(this.gitProcess.getInputStream().readAllBytes());
            if (output.equals("[]")) throw new EmptyTagListException();
            List<String> tags = new ArrayList<>(
                Stream
                    .of(output.split("\n"))
                    .filter(str -> !str.isEmpty())
                    .toList()
            );
            commit.setTags(tags);
            succeed++;
        } catch (IOException | InterruptedException e) {
            logger.warn("getPullRequestTags: Error \"{}\"", e.getMessage());
            failed++;
        } catch (EmptyTagListException | CommandExecutionFailedException e) {
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
