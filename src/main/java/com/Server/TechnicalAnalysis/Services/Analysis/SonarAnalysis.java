package com.Server.TechnicalAnalysis.Services.Analysis;

import com.Server.TechnicalAnalysis.Enums.AnalysisMetrics;
import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Services.CLI.GitCLI;
import com.Server.TechnicalAnalysis.TechnicalAnalysisApplication;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Service
public class SonarAnalysis {
    private final Logger logger = LoggerFactory.getLogger(SonarAnalysis.class);
    @Autowired
    private GitCLI gitCLI;
    private String projectOwner;
    private String projectName;
    private String repoName;
    private String sha;
    private String sonarQubeUrl;
    private String sonarQubeUser;
    private String sonarQubePassword;
    private Integer TD;
    private Integer Complexity;
    private Integer LOC;
    private String previousSHA;

    public void setParams (
            String projectOwner,
            String projectName,
            String url,
            String user,
            String pass
    )  {
        this.projectOwner = projectOwner;
        this.projectName = projectName;
        this.sonarQubeUrl = url;
        this.sonarQubeUser = user;
        this.sonarQubePassword = pass;
        String[] repoNameArray = this.projectName.split("\\\\");
        this.repoName = repoNameArray[repoNameArray.length - 1];
    }

    public void analyze(GitHubCommit commit) throws IOException, InterruptedException {
        this.sha = commit.getSha();
        //checkout
        checkoutToCommit();
        //create file
        createSonarFile();
        //remove unchanged files
//        removeUnchangedFiles();
//        this.previousSHA = this.sha;
        //start analysis
        makeSonarAnalysis();
        //Get metrics from SonarQube
        getMetricsFromSonarQube();
        //Set metrics to commit
        commit.setComplexity(this.Complexity);
        commit.setTd(this.TD);
        commit.setLoc(this.LOC);
        List<GitHubFile> files = commit.getFiles();
        for (GitHubFile file : files) {
            Map<AnalysisMetrics, Integer> metrics = getFileMetricFromSonarQube(file.getPath());
            file.setComplexity(metrics.get(AnalysisMetrics.COMPLEXITY));
            file.setTd(metrics.get(AnalysisMetrics.TD));
            file.setLoc(metrics.get(AnalysisMetrics.LOC));
        }
    }

    private void checkoutToCommit() throws IOException {
        if (TechnicalAnalysisApplication.isWindows()) {
            Process proc = Runtime.getRuntime().exec("cmd /c cd " + System.getProperty("user.dir") + "\\" + this.projectName +
                    " && git checkout " + this.sha);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                logger.info(inputLine);
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                logger.error(errorLine);
            }
        } else {
            try {
                ProcessBuilder pbuilder = new ProcessBuilder("bash", "-c",
                        "cd '" + System.getProperty("user.dir") + "/" + this.projectName + "' ; git checkout " + this.sha);
                File err = new File("err.txt");
                pbuilder.redirectError(err);
                Process p = pbuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info(line);
                }
                BufferedReader reader_2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line_2;
                while ((line_2 = reader_2.readLine()) != null) {
                    logger.info(line_2);
                }
            } catch (IOException e) {
                this.logger.error(e.getMessage(), e);
            }
        }
    }

    //Create Sonar Properties file
    private void createSonarFile() throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/" + this.projectName + "/sonar-project.properties"));
            writer.write("sonar.projectKey=" + this.projectOwner + ":" + this.repoName + System.lineSeparator());
            writer.append("sonar.projectName=").append(this.projectOwner).append(":").append(this.repoName).append(System.lineSeparator());
            writer.append("sonar.projectVersion=").append(sha.substring(0, 6)).append(System.lineSeparator());
            writer.append("sonar.sourceEncoding=UTF-8").append(System.lineSeparator());
            writer.append("sonar.sources=.").append(System.lineSeparator());
            writer.append("sonar.java.binaries=.").append(System.lineSeparator());
            writer.append("sonar.host.url=").append(this.sonarQubeUrl).append(System.lineSeparator());
            writer.append("sonar.login=").append(this.sonarQubeUser).append(System.lineSeparator());
            writer.append("sonar.password=").append(this.sonarQubePassword).append(System.lineSeparator());
        } catch (IOException e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            if (writer != null) writer.close();
        }
    }

    private void removeUnchangedFiles() {
        // git diff --name-status sha1 sha2
        // status   filename
        if (this.previousSHA == null) return;
        BufferedReader reader = this.gitCLI.runCommand("git diff --name-status " + this.sha + " "+ this.previousSHA);
        List<String> files = reader.lines().toList();
        files = files.stream().map(f->f.substring(1).strip()).toList();
        this.gitCLI.keepFiles(files);
    }

    //Start Analysis with sonar scanner
    private void makeSonarAnalysis() throws IOException, InterruptedException {
        if (TechnicalAnalysisApplication.isWindows()) {
            Process proc = Runtime.getRuntime().exec("cmd /c cd " + System.getProperty("user.dir") + "\\" + this.projectName +
                    " && ..\\..\\sonar-scanner-4.8.0.2856-windows\\bin\\sonar-scanner.bat");
            logger.info("start analysis");
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                logger.info(" {}", inputLine);
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                logger.error(errorLine);
            }
        } else {
            try {
                ProcessBuilder pbuilder = new ProcessBuilder("bash", "-c",
                        "cd '" + System.getProperty("user.dir") + "/" + this.projectName + "' ; ../sonar-scanner-4.7.0.2747-linux/bin/sonar-scanner");
                File err = new File("err.txt");
                pbuilder.redirectError(err);
                Process p = pbuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info(" {}", line);
                }
                BufferedReader reader_2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line_2;
                while ((line_2 = reader_2.readLine()) != null) {
                    logger.info(line_2);
                }
            } catch (IOException e) {
                this.logger.error(e.getMessage(), e);
            }
        }

        //wait till sonarqube finishes analysing
        while (!isFinishedAnalyzing()) {
            Thread.sleep(1000);
        }
        Thread.sleep(500);
    }

    private void getMetricsFromSonarQube() {
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest
                    .get(String.format("%s/api/measures/component?component=%s:%s&metricKeys=sqale_index,complexity,ncloc",
                            this.sonarQubeUrl, this.projectOwner, this.repoName))
                    .basicAuth(this.sonarQubeUser, this.sonarQubePassword)
                    .asString();
            if (response.getStatus() != 200)
                logger.error("MetricFromSonarQube: Status code != 200");
            else {
                JSONArray jsonarr_1 = getJsonArray(response.getRawBody());

                for (Object o : jsonarr_1) {
                    JSONObject jsonobj_1 = (JSONObject) o;
                    if (jsonobj_1.get("metric").toString().equals("sqale_index"))
                        this.TD = Integer.parseInt(jsonobj_1.get("value").toString());
                    if (jsonobj_1.get("metric").toString().equals("complexity"))
                        this.Complexity = Integer.parseInt(jsonobj_1.get("value").toString());
                    if (jsonobj_1.get("metric").toString().equals("ncloc"))
                        this.LOC = Integer.parseInt(jsonobj_1.get("value").toString());
                }
            }
        } catch (ParseException | UnirestException e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    public Map<AnalysisMetrics, Integer> getFileMetricFromSonarQube(String filePath) {
        try {
            Map<AnalysisMetrics, Integer> metrics = new HashMap<>();
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest
                    .get(String.format("%s/api/measures/component?component=%s:%s:%s&metricKeys=sqale_index,complexity,ncloc",
                            this.sonarQubeUrl, this.projectOwner, this.repoName, filePath))
                    .basicAuth(sonarQubeUser, sonarQubePassword)
                    .asString();
            if (response.getStatus() != 200)
                logger.error("FileMetricFromSonarQube (Status code != 200): {}", response.getBody());
            else {
                JSONArray jsonarr_1 = getJsonArray(response.getRawBody());

                for (Object o : jsonarr_1) {
                    JSONObject jsonobj_1 = (JSONObject) o;
                    if (jsonobj_1.get("metric").toString().equals("sqale_index"))
                        metrics.put(AnalysisMetrics.TD, Integer.parseInt(jsonobj_1.get("value").toString()));
                    if (jsonobj_1.get("metric").toString().equals("complexity"))
                        metrics.put(AnalysisMetrics.COMPLEXITY, Integer.parseInt(jsonobj_1.get("value").toString()));
                    if (jsonobj_1.get("metric").toString().equals("ncloc"))
                        metrics.put(AnalysisMetrics.LOC, Integer.parseInt(jsonobj_1.get("value").toString()));
                }
            }
            return metrics;
        } catch (ParseException | UnirestException e) {
            this.logger.error(e.getMessage(), e);
        }
        return null;
    }

    private JSONArray getJsonArray(InputStream stream) throws ParseException {
        Scanner sc = new Scanner(stream);
        StringBuilder inline = new StringBuilder();
        while (sc.hasNext()) {
            inline.append(sc.nextLine());
        }
        sc.close();

        JSONParser parse = new JSONParser();
        JSONObject jobj = (JSONObject) parse.parse(inline.toString());
        JSONObject jobj1 = (JSONObject) jobj.get("component");
        return (JSONArray) jobj1.get("measures");
    }

    /*
     * Returns if the project is finished being analyzed
     */
    public boolean isFinishedAnalyzing() {
        boolean finished = false;
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest
                    .get(sonarQubeUrl + "/api/ce/component?component=" + this.projectOwner + ":" + this.repoName)
                    .basicAuth(sonarQubeUser, sonarQubePassword)
                    .asString();
            int response_code = response.getStatus();
            if (response_code != 200)
                logger.error("FinishedAnalyzing: Status code != 200");
            else {
                Scanner sc = new Scanner(response.getRawBody());
                while (sc.hasNext()) {
                    String line = sc.nextLine();
                    if (line.trim().contains("\"analysisId\":") &&
                            line.trim().contains("\"queue\":[],")) {
                        finished = true;
                    }
                }
                sc.close();
            }
        } catch (UnirestException e) {
            this.logger.error(e.getMessage(), e);
        }
        return finished;
    }

    public Integer getLOC() {
        return this.LOC;
    }

    public Integer getTD() {
        return this.TD;
    }

    public Integer getComplexity() {
        return this.Complexity;
    }
}
