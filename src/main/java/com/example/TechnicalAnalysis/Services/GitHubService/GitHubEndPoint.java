package com.example.TechnicalAnalysis.Services.GitHubService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubEntityCollection;
import org.json.simple.parser.JSONParser;

import java.net.http.HttpClient;

public abstract class GitHubEndPoint {
    protected final HttpClient client = HttpClient.newHttpClient();
    protected final JSONParser parser = new JSONParser();
    protected GitHubEntityCollection list;
    private final String Owner = "kostasthomson";
    private final String Repo = "BlackJack";
    protected final String root_url = "https://api.github.com/repos/"+Owner+"/"+Repo+"/";
    protected final String[] headers = new String[] {
            "Accept", "application/vnd.github+json",
            "Authorization", "Bearer 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",
            "X-GitHub-Api-Version", "2022-11-28"
    };
    public abstract GitHubEntityCollection request();
    public abstract void request(GitHubCommit commit);

    public abstract void ParseResponse(String in);
}
