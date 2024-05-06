package com.Server.TechnicalAnalysis.Models;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Node("Commit")
public class GitHubCommit implements GitHubEntity, Comparable<GitHubCommit> {
    @Id
    private String sha;
    private String date;
    private String message;
    private Integer complexity;
    private Integer loc;
    private Integer td;
    @Relationship(type = "COMMITTED_BY", direction = Relationship.Direction.OUTGOING)
    private List<GitHubCollaborator> authors = new ArrayList<>();
    @Relationship(type = "CHANGED_IN", direction = Relationship.Direction.INCOMING)
    private List<GitHubFile> files;
    private List<String> tags;

    public GitHubCommit(String sha, String message, String date,
                        GitHubCollaborator author, List<GitHubFile> files, List<String> tags) {
        this.sha = sha;
        this.message = message;
        this.date = date;
        this.authors.add(author);
        this.files = files;
        this.tags = tags;
    }

    public GitHubCommit(String sha, String message, String date,
                        List<GitHubCollaborator> authors, List<GitHubFile> files, List<String> tags) {
        this.sha = sha;
        this.message = message;
        this.date = date;
        this.authors = authors;
        this.files = files;
        this.tags = tags;
    }

    public void addAuthor(GitHubCollaborator author) {
        this.authors.add(author);
    }

    @Override
    public boolean equals(Object obj) {
        return this.sha.equals(((GitHubCommit) obj).getSha());
    }

    @Override
    public int compareTo(GitHubCommit commit) {
        return LocalDateTime.parse(getDate().split("\\+")[0]).compareTo(LocalDateTime.parse(commit.getDate().split("\\+")[0]));
    }

    @Override
    public String toString() {
        return "Commit:\n\t\tsha:" + this.sha + "\n\t\tdate:" + this.date + "\n\t\tfiles:" + this.files;
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

    public List<GitHubCollaborator> getAuthors() {
        return this.authors;
    }

    public void setAuthors(List<GitHubCollaborator> authors) {
        this.authors = authors;
    }

    public List<GitHubFile> getFiles() {
        return files;
    }

    public void setFiles(List<GitHubFile> files) {
        this.files = files;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getComplexity() {
        return complexity;
    }

    public void setComplexity(Integer complexity) {
        this.complexity = complexity;
    }

    public Integer getLoc() {
        return loc;
    }

    public void setLoc(Integer loc) {
        this.loc = loc;
    }

    public Integer getTd() {
        return td;
    }

    public void setTd(Integer td) {
        this.td = td;
    }
}
