package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GitHubCollaboratorList extends GitHubEntityCollection {
    @Override
    public GitHubEntity get(String key) {
        try {
            return this.list.get(key);
        } catch (Exception ignored) {
            System.out.println("No matching collaborator");
        }
        return null;
    }

    @Override
    public void addAll(JSONArray array) {
        for (Object o : array) {
//            GitHubCollaborator collaborator = GitHubCollaborator.initializeJson((JSONObject) o);
//            list.put(collaborator.getStringId(), collaborator);
        }
    }

    @Override
    public void addAll(JsonNode array) {
        for (JsonNode obj : array) {
            this.list.put(obj.get("id").asText(), new GitHubCollaborator(obj));
        }
    }

    @Override
    public void add(GitHubEntity object) {
        GitHubCollaborator collaborator = (GitHubCollaborator) object;
        this.list.put(collaborator.getId(), collaborator);
    }

    @Override
    @NotNull
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
