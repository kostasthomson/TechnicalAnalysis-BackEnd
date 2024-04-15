package com.example.TechnicalAnalysis.Models;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;


@Node("Collaborator")
public class GitHubCollaborator implements GitHubEntity {

    @Id
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
}
