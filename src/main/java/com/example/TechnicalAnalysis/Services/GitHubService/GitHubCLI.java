package com.example.TechnicalAnalysis.Services.GitHubService;

import com.example.TechnicalAnalysis.Services.GitHubService.GitHubLogReader;
import com.example.TechnicalAnalysis.TechnicalAnalysisApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO: GitHubCLI inheritance tree (WindowsCLI and UnixCLI)
public abstract class GitHubCLI {
    private static final String CLONED_DIR_PATH = "./ClonedRepos";
    private static final File CLONED_DIR = new File(CLONED_DIR_PATH);
    private static String REPO_LINK = "";
    private static String REPO_NAME = "";
    private static List<String> REPO_LINK_LIST = new ArrayList<>();
    private static File REPO_DIR = null;
    private static File REPO_LOG = null;

    private static void setRepositoryName() {
        REPO_NAME = REPO_LINK_LIST.get(REPO_LINK_LIST.size() - 1);
    }

    private static void setRepositoryDirectory() {
        setRepositoryName();
        REPO_DIR = new File(CLONED_DIR_PATH + "/" + REPO_NAME);
    }

    private static int ExecuteCommand(String gitCommand) throws IOException, InterruptedException {
        return ExecuteCommand(gitCommand, null, false);
    }

    private static int ExecuteCommand(String gitCommand, File workingDir) throws IOException, InterruptedException {
        return ExecuteCommand(gitCommand, workingDir, false);
    }

    private static int ExecuteCommand(String gitCommand, boolean redirectOutput) throws IOException, InterruptedException {
        return ExecuteCommand(gitCommand, null, redirectOutput);
    }

    private static int ExecuteCommand(String gitCommand, File workingDir, boolean redirectOutput) throws IOException, InterruptedException {
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
            processBuilder.redirectOutput(REPO_LOG);
        else
            processBuilder.inheritIO();

        // Start the process
        Process process = processBuilder.start();

        // Wait for the process to complete
        return process.waitFor();
    }

    public static void setWorkingRepository(String link) {
        REPO_LINK = link;
        REPO_LINK_LIST = Arrays.stream(REPO_LINK.split("/")).toList();
        setRepositoryDirectory();
    }

    public static void CloneRepository() {
        // Define the Git clone command
        String gitCloneCommand = "git clone " + REPO_LINK;
        try {
            // Check if directory exists and/or create directory
            if (REPO_DIR.mkdirs()) {
                // Create a ProcessBuilder for the Git clone command
                int exitCode = ExecuteCommand(gitCloneCommand, CLONED_DIR);

                // Check the exit code
                if (exitCode == 0) {
                    System.out.println("Git clone successful.");
                } else {
                    System.err.println("Git clone failed with exit code: " + exitCode);
                }
            } else {
                System.out.println("Git repository already cloned");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception occur while cloning repository");
        }
    }

    public static void LogHistory() {
        // Git command for logging all commits in a csv format following with the files that are related to commit
        String gitLogCommand = "git --no-pager log --pretty=format:\"%H, %an, %ae %cn, %ce, %ad, %s\" --name-only";
        try {
            // Create repository's log file
            REPO_LOG = new File(CLONED_DIR_PATH + "/" + REPO_NAME + ".txt");

            // Create a ProcessBuilder for the Git log command
            int exitCode = ExecuteCommand(gitLogCommand, REPO_DIR, true);

            // Check the exit code
            if (exitCode == 0) {
                GitHubLogReader.setLogFile(REPO_LOG);
                System.out.println("Git log successful.");
            } else {
                System.err.println("Git log failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error while logging commits");
        }
    }
}
