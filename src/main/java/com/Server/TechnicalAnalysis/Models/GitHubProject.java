package com.Server.TechnicalAnalysis.Models;

import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.List;

@Node("Project")
public class GitHubProject {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;

    @Relationship(type = "CONTRIBUTE", direction = Relationship.Direction.INCOMING)
    private List<GitHubCollaborator> collaborators;

    private String name;
    private Integer complexity;
    private Integer loc;
    private Integer td;
    private Integer numFiles;
    private Integer functions;
    private Integer commentLines;
    private Integer codeSmells;
    private int maxTd;

    public GitHubProject() {
    }

    public GitHubProject(String name, List<GitHubCollaborator> collaborators, GitHubCommit commit, int maxTd) {
        this.name = name;
        this.collaborators = collaborators;
        this.complexity = commit.getComplexity();
        this.loc = commit.getLoc();
        this.td = commit.getTd();
        this.numFiles = commit.getNumFiles();
        this.functions = commit.getFunctions();
        this.commentLines = commit.getCommentLines();
        this.codeSmells = commit.getCodeSmells();
        this.maxTd = maxTd;
    }

    public Integer getNumFiles() {
        return numFiles;
    }

    public void setNumFiles(Integer numFiles) {
        this.numFiles = numFiles;
    }

    public Integer getCommentLines() {
        return commentLines;
    }

    public void setCommentLines(Integer commentLines) {
        this.commentLines = commentLines;
    }

    public Integer getCodeSmells() {
        return codeSmells;
    }

    public void setCodeSmells(Integer codeSmells) {
        this.codeSmells = codeSmells;
    }

    public int getMaxTd() {
        return maxTd;
    }

    public void setMaxTd(int maxTd) {
        this.maxTd = maxTd;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public Integer getFunctions() {
        return functions;
    }

    public void setFunctions(Integer functions) {
        this.functions = functions;
    }

    public List<GitHubCollaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<GitHubCollaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public void setCollaborators(GitHubCollaboratorList collaborators) {
        collaborators.forEach(col -> this.collaborators.add((GitHubCollaborator) col));
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
