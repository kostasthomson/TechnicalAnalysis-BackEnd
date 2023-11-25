package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;


@Node("Collaborator")
public class GitHubCollaborator implements GitHubEntity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;

    private String email;
    private String name;
    
    public GitHubCollaborator() {
        // default, no argument constructor
    }

    public GitHubCollaborator(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String toString() {
        return "Collaborator: \n\t{\n\t\tid:" + this.email + "\n\t\tname:" + this.name + "\n\t}";
    }

    public String getId() {
        return this.email;
    }

    public void setId(String new_id) {
        this.email = new_id;
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
}
