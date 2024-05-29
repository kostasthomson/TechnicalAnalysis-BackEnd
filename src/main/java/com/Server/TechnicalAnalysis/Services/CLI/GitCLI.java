package com.Server.TechnicalAnalysis.Services.CLI;

import com.Server.TechnicalAnalysis.TechnicalAnalysisApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Objects;

@Service
public class GitCLI extends SimpleCLI {
    private final Logger logger = LoggerFactory.getLogger(GitCLI.class);

    public void cloneRepository(String link) {
        // Define the Git clone command
        String gitCloneCommand = "git clone " + link;
        try {
            // Check if directory exists and/or create directory
            if (!this.repoDir.mkdirs()) throw new ProjectExistsException();
            // Create a ProcessBuilder for the Git clone command
            int exitCode = this.ExecuteCommandInDirectory(gitCloneCommand, this.REPOSITORIES_DIR);
            // Check the exit code
            if (exitCode != 0) throw new CommandExecutionFailedException(exitCode);
            logger.info("Git clone successful.");
        } catch (IOException | InterruptedException e) {
            logger.error("Exception occur while cloning repository");
        } catch (CommandExecutionFailedException e) {
            logger.error("Git clone failed with exit code {}", e.getExitCode());
        } catch (ProjectExistsException e) {
            logger.warn("Git repository already cloned");
        }
    }

    private int runCommand(String command) {
        try {
            // Create a ProcessBuilder for the Git log command
            int exitCode = this.ExecuteCommandInOutput(command);
            // Check the exit code
            if (exitCode != 0) throw new CommandExecutionFailedException(exitCode);
            logger.info("Command completed: {}", command);
            return 0;
        } catch (IOException | InterruptedException e) {
            logger.error("Unexpected error: {}", command);
        } catch (CommandExecutionFailedException e) {
            logger.error("Command execution failed with exit code {}", e.getExitCode());
        }
        return -1;
    }

    public File LogCommits() {
        String gitLogCommits = "git --no-pager log --pretty=format:\"%H, %an, %ae %cn, %ce, %ad, %s\" --name-only";
        return (this.runCommand(gitLogCommits) == 0) ? this.repoLog : null;
    }

    public File LogAuthors() {
        // Create repository's log file
        /*
        If no revisions are passed on the command line and either standard input
        is not a terminal or there is no current branch, git shortlog will output
        a summary of the log read from standard input, without reference to
        the current repository.
         */
        try {
            this.ExecuteCommand("git branch");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(this.gitProcess.getInputStream()));
            String mainBranchName = stdInput.readLine().replace("*", "").trim();
            return (this.runCommand("git shortlog -se --branches " + mainBranchName) == 0) ? this.repoLog : null;
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
        }
        return null;
    }
}