package com.Server.TechnicalAnalysis.Models;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node("File")
public class GitHubFile extends GitHubMetricEntity implements GitHubEntity {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;
    private String name;
    private String path;

    public GitHubFile() {
    }

    public GitHubFile(String fileName) {
        this.path = fileName;
        String[] nameArray = fileName.split("/");
        this.name = nameArray[nameArray.length - 1];
    }

    public GitHubFile(String name, Integer complexity, Integer loc, Integer td) {
        this.name = name;
        this.complexity = complexity;
        this.loc = loc;
        this.td = td;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object object) {
        return this.path.equals(((GitHubFile) object).path);
    }
}
