package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.JSONArray;

import java.util.HashMap;
import java.util.Map;

public abstract class GitHubEntityCollection implements Iterable<GitHubEntity> {
    protected final Map<String, GitHubEntity> list = new HashMap<>();

    public abstract GitHubEntity get(String key);

    public abstract void addAll(JSONArray array);

    public abstract void addAll(JsonNode array);

    public abstract void add(GitHubEntity object);
}
