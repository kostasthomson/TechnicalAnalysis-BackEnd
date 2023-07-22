package com.example.TechnicalAnalysis.GitHub;

import org.json.simple.JSONObject;

public class GitHubCollaborator {
    private String name;
    public GitHubCollaborator(JSONObject json) {
        name = json.get("login").toString();
    }

    @Override
    public String toString() {
        return "Collaborator: \n\t{\n\t\tname:"+name+"\n\t}";
    }
}
