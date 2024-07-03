package com.Server.TechnicalAnalysis.Services.CLI;

import com.Server.TechnicalAnalysis.TechnicalAnalysisApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public abstract class SimpleCli {
    private final Logger logger = LoggerFactory.getLogger(SimpleCli.class);
    protected final String REPOSITORIES_PATH_NAME = "Repositories";
    protected final File REPOSITORIES_DIR = new File(REPOSITORIES_PATH_NAME);
    protected File repoDir;

    public Process ExecuteCommand(String command) throws IOException, InterruptedException {
        return this.ExecuteCommand(command, null);
    }

    protected Process ExecuteCommand(String command, File workingDir) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (TechnicalAnalysisApplication.isWindows()) {
            processBuilder.command("cmd.exe", "/c", command); // For Windows systems
        } else {
            processBuilder.command("bash", "-c", command); // For Unix-like systems
        }

        // Set the working directory where the command will be executed
        if (Objects.isNull(workingDir)) processBuilder.directory(this.repoDir);
        else processBuilder.directory(workingDir);

        // Start the process
        return processBuilder.start();
    }

    public BufferedReader runCommand(String command) {
        try {
            // Create a ProcessBuilder for the Git log command
            Process process = this.ExecuteCommand(command);
            this.logger.info("Command completed: {}", command);
            return new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException | InterruptedException e) {
            this.logger.error("Unexpected error: {}", command);
        }
        return null;
    }

    public String getDirectory() {
        return this.REPOSITORIES_PATH_NAME;
    }

    public void setWorkingRepository(String repoName) {
        this.repoDir = new File(String.format("%s%s%s", this.REPOSITORIES_PATH_NAME, TechnicalAnalysisApplication.PATH_SEPARATOR, repoName));
    }

    static class ProjectExistsException extends Exception {}
    static class EmptyTagListException extends Exception {
        public EmptyTagListException() {
            super("No available tags");
        }
    }
}
