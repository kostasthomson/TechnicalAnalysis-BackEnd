package com.Server.TechnicalAnalysis.Services;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Models.GitHubMetricEntity;
import com.Server.TechnicalAnalysis.Services.CLI.GitCliService;
import com.Server.TechnicalAnalysis.TechnicalAnalysisApplication;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class SonarAnalysisService {
    private final Logger logger = LoggerFactory.getLogger(SonarAnalysisService.class);
    @Autowired
    private GitCliService gitCLI;
    @Autowired
    private HttpControllerService httpController;
    private String projectOwner;
    private String projectName;
    private String repoName;
    private String sha;
    private String sonarQubeUrl;
    private String sonarQubeUser;
    private String sonarQubePassword;
    private GitHubCommit commit;

    public void setParams(
            String projectOwner,
            String repoName,
            String url,
            String user,
            String pass
    ) {
        this.projectOwner = projectOwner;
        this.repoName = repoName;
        this.projectName = String.format("%s%s%s", this.gitCLI.getDirectory(), TechnicalAnalysisApplication.PATH_SEPARATOR, this.repoName);
        this.sonarQubeUrl = url;
        this.sonarQubeUser = user;
        this.sonarQubePassword = pass;
    }

    public void analyze(GitHubCommit c, HashMap<String, List<GitHubFile>> files) throws IOException, InterruptedException {
        this.commit = c;
        this.sha = this.commit.getSha();
        this.commit.setIsWeekCommit();
        //checkout
        checkoutToCommit();
        //create file
        createSonarFile();
        //start analysis
        makeSonarAnalysis();
        //Get metrics from SonarQube
        getMetricsFromSonarQube(files);
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
                        "cd '" + System.getProperty("user.dir") + "/" + this.projectName + "' ; ../../sonar-scanner-4.7.0.2747-linux/bin/sonar-scanner");
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

    private void populateMetricsFromComponent(GitHubMetricEntity metricEntity, JSONObject component) {
        if (component == null) return;
        JSONArray componentMeasures = (JSONArray) component.get("measures");
        for (Object object : componentMeasures) {
            JSONObject jsonObject = (JSONObject) object;
            String metric = jsonObject.get("metric").toString();
            int value = Integer.parseInt(jsonObject.get("value").toString());
            switch (metric) {
                case "sqale_index":
                    metricEntity.setTd(value);
                    break;
                case "complexity":
                    metricEntity.setComplexity(value);
                    break;
                case "ncloc":
                    metricEntity.setLoc(value);
                    break;
                case "code_smells":
                    metricEntity.setCodeSmells(value);
                    break;
                case "files":
                    metricEntity.setNumFiles(value);
                    break;
                case "functions":
                    metricEntity.setFunctions(value);
                    break;
                case "comment_lines":
                    metricEntity.setCommentLines(value);
                    break;
            }
        }
    }

    private void populateFileMetrics(HashMap<String, List<GitHubFile>> files, JSONArray components) {
        Map<String, JSONObject> objMap = new HashMap<>();
        for (Object object : components) {
            JSONObject component = (JSONObject) object;
            objMap.put(component.get("path").toString(), component);
        }
        files.forEach((key, list) -> list.forEach(file -> this.populateMetricsFromComponent(file, objMap.get(key))));
    }

    private HttpResponse<JsonNode> makeRequest(int p, int ps) throws Exception {
        HttpResponse<JsonNode> jsonNode;
        jsonNode = httpController.getRequest(new HttpControllerService.HttpRequest()
                .setUrl(this.sonarQubeUrl + "/api/measures/component_tree")
                .setParam("component", this.projectOwner + ":" + this.repoName)
                .setParam("metricKeys", "sqale_index,complexity,ncloc,code_smells,files,functions,comment_lines")
                .setParam("ps", String.valueOf(ps))
                .setParam("p", String.valueOf(p))
                .setAuth(this.sonarQubeUser, this.sonarQubePassword));
        if (Objects.isNull(jsonNode) || jsonNode.getStatus() != 200) throw new Exception("Request failed");
        return jsonNode;
    }

    private void getMetricsFromSonarQube(HashMap<String, List<GitHubFile>> files) {
        try {
            int filesPerPage = 500;
            int numOfPages = (files.size() / filesPerPage) + 1;
            Unirest.setTimeouts(0, 0);
            HttpResponse<JsonNode> jsonNode = this.makeRequest(1, filesPerPage);
            this.populateMetricsFromComponent(commit, (JSONObject) jsonNode.getBody().getObject().get("baseComponent"));
            this.populateFileMetrics(files, (JSONArray) jsonNode.getBody().getObject().get("components"));
            for (int i = 2; i <= numOfPages; i++) {
                HttpResponse<JsonNode> tmpNode = this.makeRequest(i, filesPerPage);
                this.populateFileMetrics(files, (JSONArray) tmpNode.getBody().getObject().get("components"));
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    /*
     * Returns if the project is finished being analyzed
     */
    private boolean isFinishedAnalyzing() {
        boolean finished = false;
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest
                    .get(sonarQubeUrl + "/api/ce/component?component=" + this.projectOwner + ":" + this.repoName)
                    .basicAuth(sonarQubeUser, sonarQubePassword)
                    .asString();
            int response_code = response.getStatus();
            if (response_code != 200)
                logger.error("[FinishedAnalyzing] Status code != 200 : {}", response.getBody());
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
}
