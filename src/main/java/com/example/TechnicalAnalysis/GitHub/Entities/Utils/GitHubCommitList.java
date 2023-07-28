package com.example.TechnicalAnalysis.GitHub.Entities.Utils;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCommit;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class GitHubCommitList implements GitHubEntityCollection{
    public void addAll(JSONArray array) {
        for (Object o : array) {
            String sha = ((JSONObject) o).get("sha").toString();
            list.add(new GitHubCommit(sha));
        }
    }

    public List<GitHubEntity> getList() {
        return this.list;
    }

    public void UpdateCommit(GitHubCommit e, GitHubCommit ghc) {
        ((GitHubCommit) this.list.get(this.list.indexOf(e))).Update(ghc);
    }
}
