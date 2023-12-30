package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;


@Node("Collaborator")
public class GitHubCollaborator implements GitHubEntity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;

    private String name;

    public GitHubCollaborator() {
        // default, no argument constructor
    }

    public GitHubCollaborator(String name, String email) {
        this.name = name.replace(" ", "").toLowerCase();
    }

    public GitHubCollaborator(JsonNode node) {
        this.name = node.get("login").asText().toLowerCase();
    }

    public String toString() {
        return "Collaborator: \n\t{\n\t\tname:" + this.name + "\n\t}";
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
