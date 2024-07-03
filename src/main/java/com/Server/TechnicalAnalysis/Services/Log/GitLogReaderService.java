package com.Server.TechnicalAnalysis.Services.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GitLogReaderService {
    private final Logger logger = LoggerFactory.getLogger(GitLogReaderService.class);
    private final String LOGGER_ERROR_MESSAGE = "GitLogReaderService: Couldn't read log file {}";

    public List<List<String>> readCommits(BufferedReader bufferedReader) {
        try {
            List<List<String>> commitsLogList = new ArrayList<>();
            List<String> commitsStringList = new ArrayList<>();
            String line;
            int countHeads = 0;
            while ((line=bufferedReader.readLine()) != null) {
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
            bufferedReader.close();
            return commitsLogList;
        } catch (Exception e) {
            this.logger.error(this.LOGGER_ERROR_MESSAGE, e.getMessage());
        }
        return null;
    }

    public List<String[]> readCollaborators(BufferedReader bufferedReader) {
        try {
            List<String[]> authors = new ArrayList<>();
            String line;
            while ((line=bufferedReader.readLine()) != null) {
                List<String> row = new ArrayList<>(
                    Arrays.stream(
                            line.trim()
                                .replace("\t", " ")
                                .replaceAll("[<>]", "")
                                .split(" ")
                    ).toList()
                );
                row.remove(0);
                String email = row.remove(row.size() - 1);
                String name = String.join(" ", row).toLowerCase();
                authors.add(new String[] {name, email});
            }
            bufferedReader.close();
            return authors;
        } catch (Exception e) {
            this.logger.error(this.LOGGER_ERROR_MESSAGE, e.getMessage());
        }
        return null;
    }
}
