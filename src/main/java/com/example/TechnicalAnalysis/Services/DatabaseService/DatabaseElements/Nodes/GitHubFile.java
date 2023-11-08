package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes;

import org.json.simple.JSONObject;

public class GitHubFile implements GitHubEntity {
    private final String name;

    public GitHubFile(String fileName) {
        this.name = fileName;
    }

    public GitHubFile(JSONObject json) {
        this.name = json.get("filename").toString();
    }

    public boolean isJava() {
        return this.name.endsWith(".java");
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
