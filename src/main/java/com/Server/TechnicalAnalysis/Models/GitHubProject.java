package com.Server.TechnicalAnalysis.Models;

import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.List;

@Node("Project")
public class GitHubProject extends GitHubMetricEntity {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String name;
    private int maxTd;
    @Relationship(type = "CONTRIBUTE", direction = Relationship.Direction.INCOMING)
    private List<GitHubCollaborator> collaborators;

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

    public int getMaxTd() {
        return maxTd;
    }

    public void setMaxTd(int maxTd) {
        this.maxTd = maxTd;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
