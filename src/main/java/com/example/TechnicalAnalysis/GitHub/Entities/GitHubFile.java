package com.example.TechnicalAnalysis.GitHub.Entities;

import org.json.simple.JSONObject;

public class GitHubFile implements GitHubEntity {
    private final String name;

    public GitHubFile(JSONObject json) {
        this.name = json.get("filename").toString();
    }

    public boolean isJava() {
        return this.name.endsWith(".java");
    }

    public String toString() {
        return this.name;
    }
}
