package com.example.TechnicalAnalysis.GitHub.Entities.Collections;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GitHubFileList extends GitHubEntityCollection {
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
    public String toString() {
        StringBuilder builder = new StringBuilder();
        list.forEach(builder::append);
        return builder.toString();
    }

    @Override
    public Iterator<GitHubEntity> iterator() {
        return list.iterator();
    }

    @Override
    public void forEach(Consumer<? super GitHubEntity> action) {
        list.forEach(action);
    }

    @Override
    public Spliterator<GitHubEntity> spliterator() {
        return list.spliterator();
    }
}
