package com.example.TechnicalAnalysis.Services.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GitLogReader {
    private final Logger logger = LoggerFactory.getLogger(GitLogReader.class);

    public List<List<String>> readLogFile(File logFile) {
        try {
            List<List<String>> commitsLogList = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(logFile));

            List<String> commitsStringList = new ArrayList<>();
            while (true) {
                String line = br.readLine();

                if (line == null) break;
                if (line.isEmpty()) {
                    List<String> tmp = new ArrayList<>(commitsStringList);
                    commitsLogList.add(tmp);
                    commitsStringList.clear();
                    continue;
                }
                commitsStringList.add(line);
            }
            br.close();
            return commitsLogList;
        } catch (IOException e) {
            logger.error("GitLogReader: Couldn't read log file {{}}", e.getMessage());
        }
        return null;
    }
}
