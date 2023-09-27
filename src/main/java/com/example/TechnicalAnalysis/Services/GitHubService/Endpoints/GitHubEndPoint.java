package com.example.TechnicalAnalysis.Services.GitHubService.Endpoints;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubEntityCollection;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;

import java.net.http.HttpClient;

public abstract class GitHubEndPoint {
    protected final HttpClient client = HttpClient.newHttpClient();
    protected final JSONParser parser = new JSONParser();
    protected final String[] headers = new String[]{
            "Accept", "application/vnd.github+json",
            "Authorization", "Bearer " + this.apiKey,
            "X-GitHub-Api-Version", "2022-11-28"
    };
    private final String Owner = "kostasthomson";
    private final String Repo = "BlackJack";
    protected final String root_url = "https://api.github.com/repos/" + Owner + "/" + Repo + "/";
    protected GitHubEntityCollection list;

    public abstract GitHubEntityCollection request();

    public abstract void request(GitHubCommit commit);

    public abstract void ParseResponse(String in);
}
