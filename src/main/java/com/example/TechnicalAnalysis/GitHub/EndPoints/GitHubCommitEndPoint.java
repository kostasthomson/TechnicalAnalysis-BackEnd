package com.example.TechnicalAnalysis.GitHub.EndPoints;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCommit;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class GitHubCommitEndPoint extends GitHubEndPoint {
    private GitHubCommit commit;
    public GitHubCommitEndPoint() {
        super("commits/");
    }

    @Override
    public void ParseResponse(Reader in) throws IOException, ParseException {
        Object o = parser.parse(in);
        if (o instanceof JSONObject) {
            commit = new GitHubCommit((JSONObject) o);
        }
    }

    @Override
    public void ParseResponse(String in) {
        try {
            Object o = parser.parse(in);
            if (o instanceof JSONObject) {
                commit = new GitHubCommit((JSONObject) o);
            }
        }catch (ParseException pe) {
            System.out.println("Exception: ParseException");
        }
    }

    @Override
    public void PrintResponse() {
        System.out.println(commit);
    }

    @Override
    public void PrintResponse(Void unused) {
        this.PrintResponse();
    }

    @Override
    public List<GitHubEntity> getList() {
        return null;
    }

    public GitHubCommit getCommit() {
        return this.commit;
    }

//    @Override
//    public String toString() {
//        return super.toString() + commit.getSha();
//    }
}
