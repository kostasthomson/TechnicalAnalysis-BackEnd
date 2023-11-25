package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes;

//@Node("File")
public class GitHubFile implements GitHubEntity {
    //    @Id
//    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;
    private String name;

    public GitHubFile() {
        // default, no argument constructor
    }

    public GitHubFile(String fileName) {
        this.name = fileName;
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

    @Override
    public String toString() {
        return this.name;
    }
}
