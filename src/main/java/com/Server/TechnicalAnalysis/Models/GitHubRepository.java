package com.Server.TechnicalAnalysis.Models;

import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.List;

@Node("Repository")
public class GitHubRepository {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;

    @Relationship(type = "CONTRIBUTE", direction = Relationship.Direction.INCOMING)
    private List<GitHubCollaborator> collaborators;

    private String name;
    private Integer complexity;
    private Integer loc;
    private Integer td;

    public GitHubRepository() {
    }

    public GitHubRepository(String name, List<GitHubCollaborator> collaborators, Integer complexity, Integer loc, Integer td) {
        this.name = name;
        this.collaborators = collaborators;
        this.complexity = complexity;
        this.loc = loc;
        this.td = td;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
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
