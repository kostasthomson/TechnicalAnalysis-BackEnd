package com.Server.TechnicalAnalysis.Models;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;


@Node("Collaborator")
public class GitHubCollaborator implements GitHubEntity {

    @Id
    private String email;
    private String name;

    public GitHubCollaborator() {
        // default, no argument constructor
    }

    public GitHubCollaborator(String name, String email) {
        this.name = name.replace(" ", "").toLowerCase();
        this.email = email;
    }

    public GitHubCollaborator(JsonNode node) {
        this.name = node.get("login").asText().toLowerCase();
    }

    @Override
    public String toString() {
        return "Collaborator {name: " + this.name + ", email: " + this.email + "}";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) { this.email = email;}

    public String getEmail() { return this.email; }
}
