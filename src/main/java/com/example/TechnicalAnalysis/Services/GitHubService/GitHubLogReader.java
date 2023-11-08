package com.example.TechnicalAnalysis.Services.GitHubService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GitHubLogReader {
    private static final List<List<String>> logCommits = new ArrayList<>();
    private static File LOG_FILE = null;

    public static void setLogFile(File logFile) {
        LOG_FILE = logFile;
    }

    public static void ReadLogFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(LOG_FILE));

            List<String> commitsStringList = new ArrayList<>();
            while (true) {
                String line = br.readLine();

                if (line == null) break;
                if (line.isEmpty()) {
                    List<String> tmp = new ArrayList<>(commitsStringList);
                    logCommits.add(tmp);
                    commitsStringList.clear();
                    continue;
                }
                commitsStringList.add(line);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("GitHubLogReader: Couldn't read log file {" + e.getMessage() + "}");
        }
    }

    public static List<List<String>> getLogCommits() {
        return logCommits;
    }
}
