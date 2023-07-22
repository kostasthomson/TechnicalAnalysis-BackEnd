package com.example.TechnicalAnalysis;

import com.example.TechnicalAnalysis.TechnicalAnalysisApplication;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SonarAnalysis {

    String projectOwner;
    String projectName;
    String sha;
    Integer TD;
    Integer Complexity;
    Integer LOC;
    String version;

    public SonarAnalysis(String projectOwner, String projectName, String sha,String vesrion) throws IOException, InterruptedException {
        this.projectOwner=projectOwner;
        this.projectName=projectName;
        this.sha=sha;
        this.version=vesrion;

        //checkout
        checkoutToCommit();
        //create file
        createSonarFile();
        //start analysis
        makeSonarAnalysis();
        //Get TD from SonarQube
        getMetricFromSonarQube();
    }

    private void checkoutToCommit() throws IOException {
        if(TechnicalAnalysisApplication.isWindows()) {
            Process proc = Runtime.getRuntime().exec("cmd /c cd " +System.getProperty("user.dir")+ "\\" + projectName +
                    " && git checkout " + sha + "");
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                System.out.println(inputLine);
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.out.println(errorLine);
            }
        }
        else {
            try {
                ProcessBuilder pbuilder = new ProcessBuilder("bash", "-c",
                        "cd '" + System.getProperty("user.dir") +"/"+ projectName+"' ; git checkout " + sha);
                File err = new File("err.txt");
                pbuilder.redirectError(err);
                Process p = pbuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                BufferedReader reader_2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line_2;
                while ((line_2 = reader_2.readLine()) != null) {
                    System.out.println(line_2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Create Sonar Properties file
    private void createSonarFile() throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir")+"/" + projectName + "/sonar-project.properties")));
            writer.write("sonar.projectKey=" + projectOwner +":"+ projectName + System.lineSeparator());
            writer.append("sonar.projectName=" + projectOwner +":"+ projectName + System.lineSeparator());
            writer.append("sonar.projectVersion=" + version + System.lineSeparator());
            writer.append("sonar.sourceEncoding=UTF-8" + System.lineSeparator());
            writer.append("sonar.sources=." + System.lineSeparator());
            writer.append("sonar.java.binaries=." + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    //Start Analysis with sonar scanner
    private void makeSonarAnalysis() throws IOException, InterruptedException {
        if (TechnicalAnalysisApplication.isWindows()) {
            Process proc = Runtime.getRuntime().exec("cmd /c cd " +System.getProperty("user.dir")+ "\\" + projectName +
                    " && ..\\sonar-scanner-4.7.0.2747-windows\\bin\\sonar-scanner.bat");
            System.out.println("start analysis");
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                System.out.println(" "+inputLine);
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.out.println(errorLine);
            }
        }
        else {
            try {
                ProcessBuilder pbuilder = new ProcessBuilder("bash", "-c",
                        "cd '" + System.getProperty("user.dir") +"/"+ projectName+"' ; ../sonar-scanner-4.7.0.2747-linux/bin/sonar-scanner");
                File err = new File("err.txt");
                pbuilder.redirectError(err);
                Process p = pbuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(" "+line);
                }
                BufferedReader reader_2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line_2;
                while ((line_2 = reader_2.readLine()) != null) {
                    System.out.println(line_2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //wait till sonarqube finishes analysing
        while (!isFinishedAnalyzing()) {
            Thread.sleep(1000);
        }
        Thread.sleep(500);
    }

    private void getMetricFromSonarQube() {
        try {
            URL url = new URL(TechnicalAnalysisApplication.SonarQube+"/api/measures/component?component="+projectOwner +":"+ projectName+
                    "&metricKeys=sqale_index,complexity,ncloc");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if(responsecode != 200)
                System.err.println(responsecode);
            else{
                Scanner sc = new Scanner(url.openStream());
                StringBuilder inline= new StringBuilder();
                while(sc.hasNext()){
                    inline.append(sc.nextLine());
                }
                sc.close();

                JSONParser parse = new JSONParser();
                JSONObject jobj = (JSONObject)parse.parse(inline.toString());
                JSONObject jobj1= (JSONObject) jobj.get("component");
                JSONArray jsonarr_1 = (JSONArray) jobj1.get("measures");

                for(int i=0; i<jsonarr_1.size(); i++){
                    JSONObject jsonobj_1 = (JSONObject)jsonarr_1.get(i);
                    if(jsonobj_1.get("metric").toString().equals("sqale_index"))
                        TD= Integer.parseInt(jsonobj_1.get("value").toString());
                    if(jsonobj_1.get("metric").toString().equals("complexity"))
                        Complexity= Integer.parseInt(jsonobj_1.get("value").toString());
                    if(jsonobj_1.get("metric").toString().equals("ncloc"))
                        LOC= Integer.parseInt(jsonobj_1.get("value").toString());
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /*
     * Returns if the project is finished being analyzed
     */
    public boolean isFinishedAnalyzing(){
        boolean finished=false;
        try {
            URL url = new URL(TechnicalAnalysisApplication.SonarQube+"/api/ce/component?component="+projectOwner +":"+ projectName);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int response_code = conn.getResponseCode();
            if(response_code != 200)
                throw new RuntimeException("HttpResponseCode: "+response_code);
            else{
                Scanner sc = new Scanner(url.openStream());
                while(sc.hasNext()){
                    String line=sc.nextLine();
                    if(line.trim().contains("\"analysisId\":") &&
                            line.trim().contains("\"queue\":[],")){
                        finished=true;
                    }
                }
                sc.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return finished;
    }

    public Integer getLOC(){
        return LOC;
    }
    public Integer getTD() {
        return TD;
    }
    public Integer getComplexity() {
        return Complexity;
    }
}
