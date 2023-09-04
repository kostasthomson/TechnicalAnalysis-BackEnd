package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubEntity;
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
