package com.example.TechnicalAnalysis.GitHub.Entities.Utils;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class GitHubFileList implements GitHubEntityCollection {
    @Override
    public void addAll(JSONArray array) {
        for (Object o : array) {
            GitHubFile file = new GitHubFile((JSONObject) o);
            if (file.isJava()) {
                list.add(file);
            }
        }
    }

    @Override
    public void printList() {
        System.out.println(this);
    }

    @Override
    public List<GitHubEntity> getList() {
        return this.list;
    }

}
