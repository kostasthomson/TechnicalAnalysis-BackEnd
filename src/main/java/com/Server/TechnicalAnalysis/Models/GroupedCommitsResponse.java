package com.Server.TechnicalAnalysis.Models;

import java.util.List;

public class GroupedCommitsResponse {
    private String key;
    private List<GitHubCommit> value;

    public GroupedCommitsResponse(String key, List<GitHubCommit> value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<GitHubCommit> getValue() {
        return value;
    }

    public void setValue(List<GitHubCommit> value) {
        this.value = value;
    }
}
