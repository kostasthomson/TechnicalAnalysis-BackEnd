package com.example.TechnicalAnalysis.GitHub.Entities;

import org.json.simple.JSONObject;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;


@Node("Collaborator")
public class GitHubCollaborator implements GitHubEntity {

    @Id
    @GeneratedValue
    private long node_id;

    private long id;
    private String name;
    public GitHubCollaborator(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public static GitHubCollaborator initializeJson (JSONObject json) {
        long id = Long.parseLong(json.get("id").toString());
        String name = json.get("login").toString();
        return new GitHubCollaborator(id, name);
    }

    public String toString() {
        return "Collaborator: \n\t{\n\t\tid:"+this.id+"\n\t\tname:"+this.name+"\n\t}";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
