package com.example.TechnicalAnalysis.Services.GitHubService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GitHubLogReader {
    private static File LOG_FILE = null;

    public static void setLogFile(File logFile) {
        LOG_FILE = logFile;
    }

    private static GitHubCommit createCommit(List<String> commitsList) {
// TODO: ADD METHOD BODY -> CREATE A COMMIT ACCORDING TO LIST'S ELEMENTS
        return null;
    }

    public static void read() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(LOG_FILE));

            List<GitHubCommit> commits = new ArrayList<>();
            List<String> commitsStringList = new ArrayList<>();

            while (true) {
                String line = br.readLine();

                if (line == null) break;
                if (!line.isEmpty()) commitsStringList.add(line);

                GitHubCommit currentCommit = createCommit(commitsStringList);
                commits.add(currentCommit);
                commitsStringList.clear();
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

//            Pattern commitPattern = Pattern.compile("^([0-9a-f]+),\\s(.*),\\s(.*),\\s(.+),\\s(.+)$");
//            Pattern filePattern = Pattern.compile("^([\\w/]+\\.java)$");
//
//            while ((line = br.readLine()) != null) {
//                Matcher commitMatcher = commitPattern.matcher(line);
//                if (commitMatcher.matches()) {
//                    if (currentCommit != null) {
//                        commits.add(currentCommit);
//                    }
//
//                    String hash = commitMatcher.group(1);
//                    String authorName = commitMatcher.group(2);
//                    String authorEmail = commitMatcher.group(3);
//                    String date = commitMatcher.group(4);
//                    String message = commitMatcher.group(5);
//
//                    List<String> changedFiles = new ArrayList<>();
//                    while ((line = br.readLine()) != null && filePattern.matcher(line).matches()) {
//                        changedFiles.add(line);
//                    }
//
//                    currentCommit = new GitHubCommit(hash, authorName, authorEmail, date, message, changedFiles);
//                }
//            }
//
//            // Add the last commit if there is one
//            if (currentCommit != null) {
//                commits.add(currentCommit);
//            }
//
//            // Display the parsed commit information
//            for (GitHubCommit commit : commits) {
//                System.out.println("Hash: " + commit.getSha());
//                System.out.println("Author:<" + commit.getAuthorId() + ">");
////                System.out.println("Date: " + commit.getDate());
////                System.out.println("Message: " + commit.getMessage());
////                System.out.println("Changed Files:");
////                for (String file : commit.getChangedFiles()) {
////                    System.out.println(file);
////                }
//                System.out.println();
//            }
    }
}
