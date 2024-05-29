package com.Server.TechnicalAnalysis.Services.CLI;

import com.Server.TechnicalAnalysis.TechnicalAnalysisApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public abstract class SimpleCLI {
    protected final String REPOSITORIES_PATH_NAME = "Repositories";
    protected final File REPOSITORIES_DIR = new File(REPOSITORIES_PATH_NAME);
    protected File repoLog = new File(String.format("%s\\log", this.REPOSITORIES_PATH_NAME));
    protected File repoDir;
    protected Process gitProcess;

    protected int ExecuteCommand(String command) throws IOException, InterruptedException {
        return this.ExecuteCommand(command, null, null);
    }

    protected int ExecuteCommandInDirectory(String command) throws IOException, InterruptedException {
        return this.ExecuteCommand(command, this.repoDir, null);
    }

    protected int ExecuteCommandInDirectory(String command, File workingDir) throws IOException, InterruptedException {
        return this.ExecuteCommand(command, workingDir, null);
    }

    protected int ExecuteCommandInOutput(String command) throws IOException, InterruptedException {
        return this.ExecuteCommand(command, null, this.repoLog);
    }

    protected int ExecuteCommandInOutput(String command, File redirectOutput) throws IOException, InterruptedException {
        return this.ExecuteCommand(command, null, redirectOutput);
    }

    protected int ExecuteCommand(String command, File workingDir, File redirectOutput) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (TechnicalAnalysisApplication.isWindows()) {
            processBuilder.command("cmd.exe", "/c", command); // For Windows systems
        } else {
            processBuilder.command("bash", "-c", command); // For Unix-like systems
        }

        // Set the working directory where the command will be executed
        if (Objects.isNull(workingDir)) processBuilder.directory(this.repoDir);
        else processBuilder.directory(workingDir);

        // Set the output stream where command's results will be displayed
        if (!Objects.isNull(redirectOutput))
            processBuilder.redirectOutput(redirectOutput);

        // Start the process
        this.gitProcess = processBuilder.start();

        // Wait for the process to complete
        return this.gitProcess.waitFor();
    }

    public String getDirectory() {
        return this.REPOSITORIES_PATH_NAME;
    }

    public void setWorkingRepository(String repoName) {
        this.repoDir = new File(String.format("%s\\%s", this.REPOSITORIES_PATH_NAME, repoName));
    }

    static class ProjectExistsException extends Exception {}
    static class CommandExecutionFailedException extends Exception {
        private final int exitCode;

        public CommandExecutionFailedException(int exitCode) {
            this.exitCode = exitCode;
        }

        public int getExitCode() {
            return this.exitCode;
        }
    }
    static class EmptyTagListException extends Exception {
        public EmptyTagListException() {
            super("No available tags");
        }
    }
}
