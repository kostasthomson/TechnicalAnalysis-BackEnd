package com.example.TechnicalAnalysis.GitHub.Collections;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCollaborator;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GitHubCollaboratorList extends GitHubEntityCollection {
    @Override
    public void addAll(JSONArray array) {
        for (Object o : array) {
            list.add(GitHubCollaborator.initializeJson((JSONObject) o));
        }
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
