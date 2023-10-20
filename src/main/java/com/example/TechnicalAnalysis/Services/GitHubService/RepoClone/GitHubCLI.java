package com.example.TechnicalAnalysis.Services.GitHubService.RepoClone;

import com.example.TechnicalAnalysis.TechnicalAnalysisApplication;

import java.io.File;
import java.io.IOException;

// TODO: GitHubCLI inheritance tree (WindowsCLI and UnixCLI)
public abstract class GitHubCLI {
    private static final File REPOS_DIR = new File("./ClonedRepos");

    public static void CloneRepository(String repository, String path) {
        // Define the Git clone command
        String gitCloneCommand = "git clone " + repository;
        try {
            // Create repos directory
            boolean exists = REPOS_DIR.mkdir();

            // Create a ProcessBuilder for the Git clone command
            int exitCode = ExecuteCommand(gitCloneCommand, path);

            // Check the exit code
            if (exitCode == 0) {
                System.out.println("Git clone successful.");
            } else {
                System.err.println("Git clone failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
            System.out.println("Exception occur while cloning repository");
        }
    }

    private static int ExecuteCommand(String gitCloneCommand, String targetPath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (TechnicalAnalysisApplication.isWindows()) {
            processBuilder.command("cmd.exe", "/c", gitCloneCommand); // For Windows systems
        } else {
            processBuilder.command("bash", "-c", gitCloneCommand); // For Unix-like systems
        }

        // Set the working directory where the repository will be cloned
        processBuilder.directory(REPOS_DIR);

        // Start the process
        Process process = processBuilder.start();

        // Wait for the process to complete
        return process.waitFor();
    }

    public static void PrintCommits() {
        String gitLogCommand = "git log --pretty=format:\"%H, %an, %ae %cn, %ce, %ad, %s\" --name-only";
        try {
            int exitCode = ExecuteCommand(gitLogCommand, null);

            // Check the exit code
            if (exitCode == 0) {
                System.out.println("Git log successful.");
            } else {
                System.err.println("Git log failed with exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error while logging commits");
        }
    }
}
