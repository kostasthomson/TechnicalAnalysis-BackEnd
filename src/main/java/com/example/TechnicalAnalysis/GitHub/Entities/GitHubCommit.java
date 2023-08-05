package com.example.TechnicalAnalysis.GitHub.Entities;

import com.example.TechnicalAnalysis.GitHub.Collections.GitHubFileList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

@Node("Commit")
public class GitHubCommit implements GitHubEntity {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;
    private String sha;
    private Date date;
    private String author_name;
    private long author_id;
    @Relationship(type = "COMMITED")
    private GitHubCollaborator author;
    @Autowired
    private GitHubFileList files;

    public GitHubCommit(String sha, Date date) {
        this.sha = sha;
        this.date = date;
        this.files = new GitHubFileList();
    }
    public void updateInfo(JSONObject json) {
        this.author_name = ((JSONObject) json.get("author")).get("login").toString();
        this.author_id = Long.parseLong(((JSONObject) json.get("author")).get("id").toString());
        this.files.addAll((JSONArray) json.get("files"));
    }

    private String formatDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.date);
    }

    @Override
    public String toString() {
        return "Commit:\n\t\tsha:" + this.sha + "\n\t\tauthor:" + this.author_name + "\n\t\tdate:" + this.formatDate() + "\n\t\tfiles:" + this.files;
    }

    public void Update(GitHubCommit ghc) {
        date = ghc.date;
        author_name = ghc.author_name;
        author_id = ghc.author_id;
        GitHubFileList files = ghc.files;
    }

    public boolean Is(String sha) {
        return this.sha.equals(sha);
    }

    public String getNodeId() {
        return node_id;
    }

    public void setNodeId(String node_id) {
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

    public String getAuthorName() {
        return author_name;
    }

    public void setAuthorName(String author) {
        this.author_name = author;
    }

    public long getAuthorId() {
        return author_id;
    }

    public void setAuthorId(long author_id) {
        this.author_id = author_id;
    }

    public void setAuthor(GitHubCollaborator author) {
        this.author = author;
    }

    public GitHubCollaborator getAuthor() {
        return this.author;
    }

    public GitHubFileList getFiles() {
        return files;
    }

    public void setFiles(GitHubFileList files) {
        this.files = files;
    }
}
