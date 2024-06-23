package com.Server.TechnicalAnalysis.Services;

import com.Server.TechnicalAnalysis.Enums.AnalysisMetrics;
import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Models.GitHubMetricEntity;
import com.Server.TechnicalAnalysis.Services.CLI.GitCLI;
import com.Server.TechnicalAnalysis.TechnicalAnalysisApplication;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class SonarAnalysis {
    private final Logger logger = LoggerFactory.getLogger(SonarAnalysis.class);
    @Autowired
    private GitCLI gitCLI;
    @Autowired
    private HttpController httpController;
    private String projectOwner;
    private String projectName;
    private String repoName;
    private String sha;
    private String sonarQubeUrl;
    private String sonarQubeUser;
    private String sonarQubePassword;
    private GitHubCommit commit;
    private Integer TD;
    private Integer Complexity;
    private Integer LOC;
    private Integer FILES;
    private Integer FUNCTIONS;
    private Integer COMMENT_LINES;
    private Integer CODE_SMELLS;

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

    public void analyze(GitHubCommit c, Set<GitHubFile> files) throws IOException, InterruptedException {
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

    private void populateMetricsFromComponent(GitHubMetricEntity commit, JSONObject component) {
        if (component == null) return;
        JSONArray componentMeasures = (JSONArray) component.get("measures");
        for (Object object : componentMeasures) {
            JSONObject jsonObject = (JSONObject) object;
            String metric = jsonObject.get("metric").toString();
            int value = Integer.parseInt(jsonObject.get("value").toString());
            switch (metric) {
                case "sqale_index":
                    commit.setTd(value);
                    break;
                case "complexity":
                    commit.setComplexity(value);
                    break;
                case "ncloc":
                    commit.setLoc(value);
                    break;
                case "code_smells":
                    commit.setCodeSmells(value);
                    break;
                case "files":
                    commit.setNumFiles(value);
                    break;
                case "functions":
                    commit.setFunctions(value);
                    break;
                case "comment_lines":
                    commit.setCommentLines(value);
                    break;
            }
        }
    }

    private void populateFileMetrics(Set<GitHubFile> files, JSONArray components) {
        Map<String, JSONObject> tempMap = new HashMap<>();
        for (Object object : components) {
            JSONObject component = (JSONObject) object;
            tempMap.put(component.get("path").toString(), component);
        }
        for (GitHubFile file : files) {
            this.populateMetricsFromComponent(file, tempMap.get(file.getPath()));
        }
    }

    private void getMetricsFromSonarQube(Set<GitHubFile> files) {
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<JsonNode> jsonNode = httpController.getRequest(new HttpController.HttpRequest()
                    .setUrl(this.sonarQubeUrl + "/api/measures/component_tree")
                    .setParam("component", this.projectOwner + ":" + this.repoName)
                    .setParam("metricKeys", "sqale_index,complexity,ncloc,code_smells,files,functions,comment_lines")
                    .setAuth(this.sonarQubeUser, this.sonarQubePassword));
            if (Objects.isNull(jsonNode) || jsonNode.getStatus() != 200) throw new Exception("Request failed");
            this.populateMetricsFromComponent(commit, (JSONObject) jsonNode.getBody().getObject().get("baseComponent"));
            this.populateFileMetrics(files, (JSONArray) jsonNode.getBody().getObject().get("components"));
        } catch (
                Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    public Map<AnalysisMetrics, Integer> getFileMetricFromSonarQube(String filePath) {
        try {
            Map<AnalysisMetrics, Integer> metrics = new HashMap<>();
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest
                    .get(String.format("%s/api/measures/component?component=%s:%s:%s&metricKeys=sqale_index,complexity,ncloc,code_smells,files,functions,comment_lines",
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
                    if (jsonobj_1.get("metric").toString().equals("files"))
                        metrics.put(AnalysisMetrics.FILES, Integer.parseInt(jsonobj_1.get("value").toString()));
                    if (jsonobj_1.get("metric").toString().equals("functions"))
                        metrics.put(AnalysisMetrics.FUNCTIONS, Integer.parseInt(jsonobj_1.get("value").toString()));
                    if (jsonobj_1.get("metric").toString().equals("comment_lines"))
                        metrics.put(AnalysisMetrics.COMMENT_LINES, Integer.parseInt(jsonobj_1.get("value").toString()));
                    if (jsonobj_1.get("metric").toString().equals("code_smells"))
                        metrics.put(AnalysisMetrics.CODE_SMELLS, Integer.parseInt(jsonobj_1.get("value").toString()));
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

    public Integer getLOC() {
        return this.LOC;
    }

    public Integer getTD() {
        return this.TD;
    }

    public Integer getComplexity() {
        return this.Complexity;
    }

    public Integer getFILES() {
        return FILES;
    }

    public Integer getFUNCTIONS() {
        return FUNCTIONS;
    }

    public Integer getCOMMENT_LINES() {
        return COMMENT_LINES;
    }

    public Integer getCODE_SMELLS() {
        return CODE_SMELLS;
    }

    public String getSonarQubeUrl() {
        return sonarQubeUrl;
    }

    public String getSonarQubeUser() {
        return sonarQubeUser;
    }

    public String getSonarQubePassword() {
        return sonarQubePassword;
    }

    public String getProjectOwner() {
        return projectOwner;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getRepoName() {
        return repoName;
    }
}
