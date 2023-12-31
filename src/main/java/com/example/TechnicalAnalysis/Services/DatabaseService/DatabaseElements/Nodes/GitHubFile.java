package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node("File")
public class GitHubFile implements GitHubEntity {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;
    private String name;
    @Relationship(type = "CHANGED_IN", direction = Relationship.Direction.OUTGOING)
    private GitHubCommit commit;

    public GitHubFile() {
        // default, no argument constructor
    }

    public GitHubFile(String fileName) {
        this.name = fileName;
    }

    public GitHubFile(String fileName, GitHubCommit commit) {
        this.name = fileName;
        this.commit = commit;
    }

    public boolean isJava() {
        return this.name.endsWith(".java");
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public GitHubCommit getCommit() {
        return this.commit;
    }

    public void setCommit(GitHubCommit newCommit) {
        this.commit = newCommit;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
