package com.example.TechnicalAnalysis.Services.CLI;

import com.example.TechnicalAnalysis.TechnicalAnalysisApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class GitHubCLI {
    private final Logger logger = LoggerFactory.getLogger(GitHubCLI.class);
    private int succeed = 0;
    private int failed = 0;

    public List<String> getPullRequestTags(String sha) {
        List<String> tags = new ArrayList<>();
        // Start the process
        try {
            ProcessBuilder processBuilder = getProcessBuilder(sha);
            Process process = processBuilder.start();
            int code = process.waitFor();
            if (code == 0) {
                String output = new String(process.getInputStream().readAllBytes());
                if (output.equals("[]")) throw new EmptyTagListException();
                tags.addAll(
                        Stream.of(output.split("\n"))
                                .filter(str -> !str.isEmpty())
                                .toList()
                );
                succeed++;
            } else {
                throw new ProcessCodeException(code);
            }
        } catch (IOException | InterruptedException e) {
            logger.warn("getPullRequestTags: Error \"{}\"", e.getMessage());
            failed++;
        } catch (EmptyTagListException | ProcessCodeException e) {
            failed++;
        }
        return tags;
    }

    private ProcessBuilder getProcessBuilder(String sha) {
        String ghCommand = String.format("gh pr list --search \"%s\" --state merged --json labels --jq .[0].labels[].name", sha);

        ProcessBuilder processBuilder = new ProcessBuilder();
        if (TechnicalAnalysisApplication.isWindows()) {
            processBuilder.command("cmd.exe", "/c", ghCommand); // For Windows systems
        } else {
            processBuilder.command("bash", "-c", ghCommand); // For Unix-like systems
        }
        // Set the working directory where the git command will be executed
        processBuilder.directory(new File("ClonedRepos/BlackJack"));
        return processBuilder;
    }

    public int getSucceed() {
        return succeed;
    }

    public int getFailed() {
        return failed;
    }

    private static class EmptyTagListException extends Exception {
        public EmptyTagListException() {
            super("No available tags");
        }
    }

    private static class ProcessCodeException extends Exception {
        public ProcessCodeException(int code) {
            super("Process code " + code);
        }
    }
}
