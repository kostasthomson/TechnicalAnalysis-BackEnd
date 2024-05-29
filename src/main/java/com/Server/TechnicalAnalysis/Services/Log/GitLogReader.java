package com.Server.TechnicalAnalysis.Services.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GitLogReader {
    private final Logger logger = LoggerFactory.getLogger(GitLogReader.class);
    private final String LOGGER_ERROR_MESSAGE = "GitLogReader: Couldn't read log file {}";

    public List<List<String>> readCommits(File logFile) {
        try {
            List<List<String>> commitsLogList = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(logFile));

            List<String> commitsStringList = new ArrayList<>();
            String line;
            int countHeads = 0;
            while ((line=br.readLine()) != null) {
                if (line.isEmpty()) continue;
                if (line.contains(",")) {
                    countHeads++;
                    if (countHeads == 2) {
                        commitsLogList.add(new ArrayList<>(commitsStringList));
                        commitsStringList.clear();
                        countHeads = 1;
                    }
                }
                commitsStringList.add(line);
            }
            br.close();
            return commitsLogList;
        } catch (Exception e) {
            this.logger.error(this.LOGGER_ERROR_MESSAGE, e.getMessage());
        }
        return null;
    }

    public List<String[]> readCollaborators(File f) {
        try {
            List<String[]> authors = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (true) {
                String line = br.readLine();

                if (line == null) break;
                List<String> row = new ArrayList<>(
                        Arrays.stream(line
                                .trim()
                                .replace("\t", " ")
                                .replaceAll("[<>]", "")
                                .split(" "))
                        .toList()
                );
                row.remove(0);
                String email = row.remove(row.size() - 1);
                String name = String.join(" ", row).toLowerCase();
                authors.add(new String[] {name, email});
            }
            br.close();
            return authors;
        } catch (Exception e) {
            this.logger.error(this.LOGGER_ERROR_MESSAGE, e.getMessage());
        }
        return null;
    }
}
