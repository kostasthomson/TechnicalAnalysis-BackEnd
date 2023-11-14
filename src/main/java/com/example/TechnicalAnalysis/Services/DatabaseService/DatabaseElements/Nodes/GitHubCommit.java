package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.GitHubFileList;
import com.example.TechnicalAnalysis.Services.GitHubService.GitHubCollaboratorBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Node("Commit")
public class GitHubCommit implements GitHubEntity {
    @Id
    private String sha;
    private LocalDateTime date;
    private String author_id;
    private String message;
    @Relationship(type = "COMMITTED_BY", direction = Relationship.Direction.OUTGOING)
    private GitHubCollaborator author;
    @Autowired
    private GitHubFileList files;

    public GitHubCommit(String sha, String author_id) {
        this.sha = sha;
        this.author_id = author_id;
    }

    public GitHubCommit(List<String> commitInfo, List<String> commitFiles) {
        this.sha = commitInfo.get(0);
        this.author_id = commitInfo.get(3);
        this.message = commitInfo.get(5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM d HH:mm:ss yyyy Z");
        this.date = LocalDateTime.parse(commitInfo.get(4), formatter);

        this.author = GitHubCollaboratorBuilder.getCollaborator(this.author_id, commitInfo.get(1));
//        this.files = new GitHubFileList(commitFiles);
    }

    public void updateInfo(JSONObject json) {
        this.files.addAll((JSONArray) json.get("files"));
    }

    @Override
    public String toString() {
        return "Commit:\n\t\tsha:" + this.sha + "\n\t\tdate:" + this.date + "\n\t\tfiles:" + this.files;
    }

    public String getSha() {
        return this.sha;
    }

    public void setSha(String new_sha) {
        this.sha = new_sha;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


    public String getAuthorId() {
        return author_id;
    }

    public void setAuthorId(String author_id) {
        this.author_id = author_id;
    }

    public GitHubCollaborator getAuthor() {
        return this.author;
    }

    public void setAuthor(GitHubCollaborator author) {
        this.author = author;
    }

    public GitHubFileList getFiles() {
        return files;
    }

    public void setFiles(GitHubFileList files) {
        this.files = files;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String new_message) {
        this.message = new_message;
    }
}
