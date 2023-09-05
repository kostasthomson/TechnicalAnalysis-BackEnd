package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubEntity;
import org.json.simple.JSONArray;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GitHubFileList extends GitHubEntityCollection {
    @Override
    public GitHubEntity get(String key) {
        return null;
    }

    @Override
    public void addAll(JSONArray array) {
//        for (Object o : array) {
//            GitHubFile file = new GitHubFile((JSONObject) o);
//            if (file.isJava()) {
//                list.add(file);
//            }
//        }
    }

    @Override
    public Iterator<GitHubEntity> iterator() {
        return list.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super GitHubEntity> action) {
        list.values().forEach(action);
    }

    @Override
    public Spliterator<GitHubEntity> spliterator() {
        return list.values().spliterator();
    }
}
