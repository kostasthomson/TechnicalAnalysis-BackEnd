package com.Server.TechnicalAnalysis.Services.CLI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class GitCLI extends SimpleCLI {
    private final Logger logger = LoggerFactory.getLogger(GitCLI.class);
    private final String GIT_COMMAND = "git";

    public void cloneRepository(String link) {
        // Define the Git clone command
        String gitCloneCommand = GIT_COMMAND + " clone " + link;
        try {
            // Check if directory exists and/or create directory
            if (!this.repoDir.mkdirs()) throw new ProjectExistsException();
            // Create a ProcessBuilder for the Git clone command
            this.ExecuteCommand(gitCloneCommand, this.REPOSITORIES_DIR).waitFor();
            this.logger.info("Git clone successful.");
        } catch (ProjectExistsException e) {
            this.logger.warn("Git repository already cloned");
        } catch (IOException | InterruptedException e) {
            this.logger.error("Exception occur while cloning repository");
        }
    }

    public BufferedReader LogCommits() {
        String gitLogCommits = GIT_COMMAND + " --no-pager log --pretty=format:\"%H, %an, %ae %cn, %ce, %ad, %s\" --name-only";
        return this.runCommand(gitLogCommits);
    }

    public BufferedReader LogAuthors() throws IOException {
        // Create repository's log file
        /*
        If no revisions are passed on the command line and either standard input
        is not a terminal or there is no current branch, git shortlog will output
        a summary of the log read from standard input, without reference to
        the current repository.
         */
        BufferedReader stdInput = this.runCommand(GIT_COMMAND + " branch");
        String mainBranchName = Objects.requireNonNull(stdInput).readLine().replace("*", "").trim();
        return this.runCommand(GIT_COMMAND + " shortlog -se --branches " + mainBranchName);
    }

    public void keepFiles(List<String> keepFiles) {
        File[] dirFiles = this.repoDir.listFiles();
        if (dirFiles == null) return;
        for (File file : dirFiles) {
            String name = file.getName();
            if (!name.endsWith(".java")) continue;
            if (!keepFiles.contains(name) && file.isFile()) {
                if (file.delete()) this.logger.info("File deleted: {}", name);
                else this.logger.warn("File failed: {}", name);
            }
        }
    }
}