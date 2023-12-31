package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseController;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.GitHubFileList;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Node("Commit")
public class GitHubCommit implements GitHubEntity {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;
    private String sha;
    private String date;
    private String message;
    @Relationship(type = "COMMITTED_BY", direction = Relationship.Direction.OUTGOING)
    private GitHubCollaborator author;
    private GitHubFileList files;

    public GitHubCommit() {
        // default, no argument constructor
    }

    public GitHubCommit(List<String> commitInfo, List<String> commitFiles) {
        this.sha = commitInfo.get(0);
        this.message = commitInfo.get(5);

        // Define the formatter for the input date string
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("E MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);

        // Parse the input date string
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(commitInfo.get(4), inputFormatter);

        // Convert to ISO-8601 format
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;
        this.date = offsetDateTime.format(isoFormatter);

        this.author = DatabaseController.findCollaborator(commitInfo.get(1));
        this.files = new GitHubFileList(commitFiles, this);

    }


    @Override
    public String toString() {
        return "Commit:\n\t\tsha:" + this.sha + "\n\t\tdate:" + this.date + "\n\t\tfiles:" + this.files;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GitHubCollaborator getAuthor() {
        return author;
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
}
