package com.example.TechnicalAnalysis.GitHub.Entities.Utils;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCollaborator;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class GitHubCollaboratorList implements GitHubEntityCollection {
    @Override
    public void addAll(JSONArray array) {
        for (Object o : array) {
            list.add(new GitHubCollaborator((JSONObject) o));
        }
    }

    @Override
    public List<GitHubEntity> getList() {
        return this.list;
    }
}
