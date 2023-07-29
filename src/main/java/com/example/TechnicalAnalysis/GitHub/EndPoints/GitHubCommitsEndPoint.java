package com.example.TechnicalAnalysis.GitHub.EndPoints;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCommit;
import com.example.TechnicalAnalysis.GitHub.Collections.GitHubCommitList;
import com.example.TechnicalAnalysis.GitHub.Collections.GitHubEntityCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GitHubCommitsEndPoint extends GitHubEndPoint {
    private final String name = "commits";
    @Override
    public GitHubEntityCollection request() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(root_url + this.name))
                .headers(headers)
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::ParseResponse)
                .join();
        return list;
    }

    @Override
    public void request(GitHubCommit commit) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(root_url + this.name + "/" + commit.getSha()))
                .headers(headers)
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::ParseCommitResponse)
                .join();
    }

    public void UpdateCommit(String sha, JSONObject jsonInfo) {
        ((GitHubCommitList) list).get(sha).updateInfo(jsonInfo);
    }

    public void ParseCommitResponse(String in) {
        try {
            Object o = parser.parse(in);
            if (o instanceof JSONObject json) {
                String sha = json.get("sha").toString();
                this.UpdateCommit(sha, json);
            }
        }catch (ParseException pe) {
            System.out.println("Exception: ParseException");
        }
    }

    @Override
    public void ParseResponse(String in) {
        list = new GitHubCommitList();
        try {
            Object o = parser.parse(in);
            if (o instanceof JSONArray) {
                list.addAll((JSONArray) o);
            }
        } catch (ParseException pe) {
            System.out.println("Exception: ParseException");
        }
    }
}
