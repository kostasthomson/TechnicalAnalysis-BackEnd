package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes;

import org.json.simple.JSONObject;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;


@Node("Collaborator")
public class GitHubCollaborator implements GitHubEntity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;

    private long id;
    private String name;

    public GitHubCollaborator(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static GitHubCollaborator initializeJson(JSONObject json) {
        long id = Long.parseLong(json.get("id").toString());
        String name = json.get("login").toString();
        return new GitHubCollaborator(id, name);
    }

    public String toString() {
        return "Collaborator: \n\t{\n\t\tid:" + this.id + "\n\t\tname:" + this.name + "\n\t}";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStringId() {
        return String.valueOf(this.id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasId(long authorId) {
        return this.id == authorId;
    }

    public boolean hasId(String authorId) {
        return authorId.equals(String.valueOf(this.id));
    }
}
