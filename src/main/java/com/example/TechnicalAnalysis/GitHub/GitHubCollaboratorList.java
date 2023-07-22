package com.example.TechnicalAnalysis.GitHub;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public abstract class GitHubCollaboratorList {
    private static ArrayList<GitHubCollaborator> collaboratorList = new ArrayList<>();
    public static void addJsonCollaborator(JSONObject json) {
        collaboratorList.add(new GitHubCollaborator(json));
    }
    public static void addAll(JSONArray array) {
        for (int i = 0; i < array.size(); i++) {
            GitHubCollaboratorList.addJsonCollaborator( (JSONObject) array.get(i));
        }
    }

    public static void PrintList() {
        System.out.println("("+collaboratorList.size()+")[");
        for (GitHubCollaborator c : collaboratorList) {
            System.out.println("\t"+c.toString());
        }
        System.out.println("]");
    }
}
