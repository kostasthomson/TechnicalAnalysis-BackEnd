package com.example.TechnicalAnalysis.GitHub.Entities;

import org.json.simple.JSONObject;

public class GitHubCollaborator implements GitHubEntity {
    private long id;
    private String name;
    public GitHubCollaborator(JSONObject json) {
        this.id = Long.parseLong(json.get("id").toString());
        this.name = json.get("login").toString();
    }

    public String toString() {
        return "Collaborator: \n\t{\n\t\tid:"+this.id+"\n\t\tname:"+this.name+"\n\t}";
    }
}
