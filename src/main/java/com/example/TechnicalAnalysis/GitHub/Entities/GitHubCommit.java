package com.example.TechnicalAnalysis.GitHub.Entities;

import com.example.TechnicalAnalysis.GitHub.Entities.Collections.GitHubFileList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.text.SimpleDateFormat;
import java.util.Date;

@Node("Commit")
public class GitHubCommit implements GitHubEntity {
    @Id
    @GeneratedValue
    private long node_id;
    private String sha;
    private Date date;
    private String author;
    private long author_id;
    @Autowired
    private GitHubFileList files;

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

    public boolean Is(String sha) {
        return this.sha.equals(sha);
    }

    public long getNode_id() {
        return node_id;
    }

    public void setNode_id(long node_id) {
        this.node_id = node_id;
    }

    public String getSha() {
        return this.sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(long author_id) {
        this.author_id = author_id;
    }

    public GitHubFileList getFiles() {
        return files;
    }

    public void setFiles(GitHubFileList files) {
        this.files = files;
    }
}
