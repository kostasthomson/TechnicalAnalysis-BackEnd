package com.Server.TechnicalAnalysis.Models;

import java.util.List;

public class GroupedCommitsResponse {
    private String key;
    private int maxTd;
    private List<GitHubCommit> value;

    public GroupedCommitsResponse(String key, int maxTd,  List<GitHubCommit> value) {
        this.key = key;
        this.maxTd = maxTd;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public int getMaxTd() {
        return maxTd;
    }

    public void setMaxTd(int maxTd) {
        this.maxTd = maxTd;
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
