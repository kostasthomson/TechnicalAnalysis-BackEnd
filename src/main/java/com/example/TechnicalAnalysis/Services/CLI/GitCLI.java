package com.example.TechnicalAnalysis.Services.CLI;

import com.example.TechnicalAnalysis.TechnicalAnalysisApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GitCLI {
    private final Logger logger = LoggerFactory.getLogger(GitCLI.class);
    private final String CLONED_DIR_PATH = "./ClonedRepos";
    private final File CLONED_DIR = new File(CLONED_DIR_PATH);
    private String repoLink;
    private String repoName;
    private List<String> repoLinkList = new ArrayList<>();
    private File repoDir;
    private File repoLogFile;

    private void setRepositoryName() {
        this.repoName = this.repoLinkList.get(this.repoLinkList.size() - 1);
    }

    private void setRepositoryDirectory() {
        this.setRepositoryName();
        this.repoDir = new File(String.format("%s/%s", this.CLONED_DIR_PATH, this.repoName));
    }

    private int ExecuteCommand(String gitCommand) throws IOException, InterruptedException {
        return this.ExecuteCommand(gitCommand, null, false);
    }

    private int ExecuteCommand(String gitCommand, File workingDir) throws IOException, InterruptedException {
        return this.ExecuteCommand(gitCommand, workingDir, false);
    }

    private int ExecuteCommand(String gitCommand, boolean redirectOutput) throws IOException, InterruptedException {
        return this.ExecuteCommand(gitCommand, null, redirectOutput);
    }

    private int ExecuteCommand(String gitCommand, File workingDir, boolean redirectOutput) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (TechnicalAnalysisApplication.isWindows()) {
            processBuilder.command("cmd.exe", "/c", gitCommand); // For Windows systems
        } else {
            processBuilder.command("bash", "-c", gitCommand); // For Unix-like systems
        }

        // Set the working directory where the git command will be executed
        processBuilder.directory(workingDir);

        // Set the output stream where command's results will be displayed
        if (redirectOutput)
            processBuilder.redirectOutput(this.repoLogFile);
        else
            processBuilder.inheritIO();

        // Start the process
        Process process = processBuilder.start();

        // Wait for the process to complete
        return process.waitFor();
    }

    private void setWorkingRepository(String link) {
        this.repoLink = link;
        this.repoLinkList = Arrays.stream(this.repoLink.split("/")).toList();
        this.setRepositoryDirectory();
    }

    private void CloneRepository() {
        // Define the Git clone command
        String gitCloneCommand = "git clone " + this.repoLink;
        try {
            // Check if directory exists and/or create directory
            if (this.repoDir.mkdirs()) {
                // Create a ProcessBuilder for the Git clone command
                int exitCode = this.ExecuteCommand(gitCloneCommand, this.CLONED_DIR);

                // Check the exit code
                if (exitCode == 0) {
                    logger.info("Git clone successful.");
                } else {
                    logger.warn("Git clone failed with exit code: {}", exitCode);
                }
            } else {
                logger.warn("Git repository already cloned");
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Exception occur while cloning repository");
        }
    }

    private File LogCommits() {
        // Git command for logging all commits in a csv format following with the files that are related to commit
        String gitLogCommand = "git --no-pager log --pretty=format:\"%H, %an, %ae %cn, %ce, %ad, %s\" --name-only";
        try {
            // Create repository's log file
            this.repoLogFile = new File(String.format("%s/%s.txt", this.CLONED_DIR_PATH, this.repoName));

            // Create a ProcessBuilder for the Git log command
            int exitCode = this.ExecuteCommand(gitLogCommand, this.repoDir, true);

            // Check the exit code
            if (exitCode == 0) {
                logger.info("Git log successful.");
                return this.repoLogFile;
            } else {
                logger.warn("Git log failed with exit code: {}", exitCode);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error while logging commits");
        }
        return null;
    }

    public File createLog(String link) {
        this.setWorkingRepository(link);
        this.CloneRepository();
        return this.LogCommits();
    }
}
