package com.example.TechnicalAnalysis.GitHub.RequestMethods;

import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class GitHubCLI implements GitHubRequest {
    public static void GetCollaborators() {
        //Getting Repo collaborators GitHubCLI
        try {
            Process process_col = Runtime.getRuntime().exec("cmd /c gh api /repos/"+owner+"/"+repo+"/"+collaborators);
            BufferedReader inputReader_col = new BufferedReader(new InputStreamReader(process_col.getInputStream()));
            collaborators.ParseResponse(inputReader_col);
            collaborators.PrintResponse();
        } catch (IOException e) {
            System.out.println("Exception: IOException in GitHubController.CliRequestCollaborators");
        } catch (ParseException e) {
            System.out.println("Exception: ParseException in GitHubController.CliRequestCollaborators");
        }
    }

    public static void GetCommits() {
        //Getting Repo commits GitHubCLI
        try {
            Process process_col = Runtime.getRuntime().exec("cmd /c gh api /repos/"+owner+"/"+repo+"/"+commits);
            BufferedReader inputReader_col = new BufferedReader(new InputStreamReader(process_col.getInputStream()));
            commits.ParseResponse(inputReader_col);
            commits.PrintResponse();
        } catch (IOException e) {
            System.out.println("Exception: IOException in GitHubController.CliRequestCommits");
        } catch (ParseException e) {
            System.out.println("Exception: ParseException in GitHubController.CliRequestCommits");
        }
    }

    public static void GetCommit(String ghc_sha) {
        //Getting Repo commit GitHubCLI
        try {
            Process process_col = Runtime.getRuntime().exec("cmd /c gh api /repos/"+owner+"/"+repo+"/"+commits+"/"+ghc_sha);
            BufferedReader inputReader_col = new BufferedReader(new InputStreamReader(process_col.getInputStream()));
            commits.ParseResponse(inputReader_col);
            commits.PrintResponse();
        } catch (IOException e) {
            System.out.println("Exception: IOException in GitHubController.CliRequestCommits");
        } catch (ParseException e) {
            System.out.println("Exception: ParseException in GitHubController.CliRequestCommits");
        }
    }
}
