package com.Server.TechnicalAnalysis.Models;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.List;
import java.util.Objects;

@Node("File")
public class GitHubFile extends GitHubMetricEntity implements GitHubEntity {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;
    private String name;
    private String path;
    private List<String> tags;

    public GitHubFile() {
    }

    public GitHubFile(String fileName) {
        this.path = fileName;
        String[] nameArray = fileName.split("/");
        this.name = nameArray[nameArray.length - 1];
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

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return this.tags;
    }

    @Override
    public String toString() {
        return this.path;
    }

    @Override
    public boolean equals(Object object) {
        return this.path.equals(((GitHubFile) object).path);
    }

    @Override
    public int hashCode() {
        return this.path.hashCode();
    }
}
