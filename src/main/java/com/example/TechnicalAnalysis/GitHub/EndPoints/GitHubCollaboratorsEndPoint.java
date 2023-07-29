package com.example.TechnicalAnalysis.GitHub.EndPoints;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCommit;
import com.example.TechnicalAnalysis.GitHub.Collections.GitHubCollaboratorList;
import com.example.TechnicalAnalysis.GitHub.Collections.GitHubEntityCollection;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GitHubCollaboratorsEndPoint extends GitHubEndPoint {
    private final String name = "collaborators";
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

    }

    @Override
    public void ParseResponse(String in) {
        list = new GitHubCollaboratorList();
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
