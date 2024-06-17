package com.Server.TechnicalAnalysis.Models;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
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
    private Integer numFiles;
    private Integer functions;
    private Integer commentLines;
    private Integer codeSmells;
    @Relationship(type = "COMMITTED_BY", direction = Relationship.Direction.OUTGOING)
    private GitHubCollaborator author;
    @Relationship(type = "CHANGED_IN", direction = Relationship.Direction.INCOMING)
    private List<GitHubFile> files;
    private List<String> tags;
    private String projectName;

    public GitHubCommit() {
    }

    public GitHubCommit(String sha, String message, String date,
                        GitHubCollaborator author, List<GitHubFile> files) {
        this.sha = sha;
        this.message = message;
        this.date = date;
        this.author = author;
        this.files = files;
    }

    public GitHubCommit(String sha, String message, String date,
                        GitHubCollaborator author, List<GitHubFile> files, List<String> tags) {
        this.sha = sha;
        this.message = message;
        this.date = date;
        this.author = author;
        this.files = files;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object obj) {
        return this.sha.equals(((GitHubCommit) obj).getSha());
    }

    @Override
    public int compareTo(GitHubCommit commit) {
        LocalDateTime thisDate = LocalDateTime.parse(this.date.substring(0, 19));
        LocalDateTime otherDate = LocalDateTime.parse(commit.date.substring(0, 19));
        return thisDate.compareTo(otherDate);
    }

    @Override
    public String toString() {
        return "Commit:\n\t\tsha:" + this.sha + "\n\t\tdate:" + this.date + "\n\t\tfiles:" + this.files;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getNumFiles() {
        return numFiles;
    }

    public void setNumFiles(Integer num_files) {
        this.numFiles = num_files;
    }

    public Integer getFunctions() {
        return functions;
    }

    public void setFunctions(Integer functions) {
        this.functions = functions;
    }

    public Integer getCommentLines() {
        return commentLines;
    }

    public void setCommentLines(Integer comment_lines) {
        this.commentLines = comment_lines;
    }

    public Integer getCodeSmells() {
        return codeSmells;
    }

    public void setCodeSmells(Integer code_smells) {
        this.codeSmells = code_smells;
    }

    public GitHubCollaborator getAuthor() {
        return author;
    }

    public void setAuthor(GitHubCollaborator author) {
        this.author = author;
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
