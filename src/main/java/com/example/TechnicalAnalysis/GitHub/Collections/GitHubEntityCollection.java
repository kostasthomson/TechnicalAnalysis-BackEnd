package com.example.TechnicalAnalysis.GitHub.Collections;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public abstract class GitHubEntityCollection  implements Iterable<GitHubEntity>{
    protected final List<GitHubEntity> list = new ArrayList<>();
    public abstract void addAll(JSONArray array);
    public void printList() {
        System.out.println("("+list.size()+")[");
        for (GitHubEntity e : list) {
            System.out.println("\t"+e);
        }
        System.out.println("]");
    }
}
