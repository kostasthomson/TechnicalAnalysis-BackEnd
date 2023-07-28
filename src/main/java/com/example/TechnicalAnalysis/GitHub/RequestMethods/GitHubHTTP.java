package com.example.TechnicalAnalysis.GitHub.RequestMethods;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class GitHubHTTP implements GitHubRequest {
    public static void GetCollaborators() {
        //Getting Repo collaborators GitHub http request
        String url = base_url + collaborators;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(headers)
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(collaborators::ParseResponse)
                .thenAccept(collaborators::PrintResponse)
                .join();
    }

    public static void GetCommits() {
        //Getting Repo commits GitHub http request
        String url = base_url + commits;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(headers)
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(commits::ParseResponse)
//                .thenAccept(commits::PrintResponse)
                .join();
    }

    public static void GetCommit(String ghc_sha) {
        //Getting Repo commit GitHub http request
        String url = base_url + commit + ghc_sha;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(headers)
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(commit::ParseResponse)
//                .thenAccept(commit::PrintResponse)
                .join();
    }
}
