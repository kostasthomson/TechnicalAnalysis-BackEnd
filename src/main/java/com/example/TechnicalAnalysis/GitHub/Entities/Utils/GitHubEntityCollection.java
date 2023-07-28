package com.example.TechnicalAnalysis.GitHub.Entities.Utils;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public interface GitHubEntityCollection {
    List<GitHubEntity> list = new ArrayList<>();
    void addAll(JSONArray array);
    default void printList() {
        System.out.println("("+list.size()+")[");
        for (GitHubEntity e : list) {
            System.out.println("\t"+e);
        }
        System.out.println("]");
    }

    default String string() {
        StringBuilder builder = new StringBuilder("("+list.size()+")[");
        for (GitHubEntity e : list) {
            builder.append("\t").append(e);
        }
        builder.append("]");
        return builder.toString();
    }
    List<GitHubEntity> getList();
}
