package com.example.TechnicalAnalysis.GitHub.Entities;

import com.example.TechnicalAnalysis.GitHub.Entities.Collections.GitHubFileList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GitHubCommit implements GitHubEntity {
    private String sha;
    private Date date;
    private String author;
    private long author_id;
    private GitHubFileList files;

    public GitHubCommit(String sha) {
        this.sha = sha;
        this.files = new GitHubFileList();
    }
    public GitHubCommit(String sha, Date date) {
        this.sha = sha;
        this.date = date;
        this.files = new GitHubFileList();
    }
    public void updateInfo(JSONObject json) {
        this.author = ((JSONObject) json.get("author")).get("login").toString();
        this.author_id = Long.parseLong(((JSONObject) json.get("author")).get("id").toString());
        this.files.addAll((JSONArray) json.get("files"));
    }

    private String formatDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.date);
    }

    @Override
    public String toString() {
        return "Commit:\n\t\tsha:" + this.sha + "\n\t\tauthor:" + this.author + "\n\t\tdate:" + this.formatDate() + "\n\t\tfiles:" + this.files;
    }

    public void Update(GitHubCommit ghc) {
        date = ghc.date;
        author = ghc.author;
        author_id = ghc.author_id;
        GitHubFileList files = ghc.files;
    }

    public String getSha() {
        return this.sha;
    }

    public boolean Is(String sha) {
        return this.sha.equals(sha);
    }
}
