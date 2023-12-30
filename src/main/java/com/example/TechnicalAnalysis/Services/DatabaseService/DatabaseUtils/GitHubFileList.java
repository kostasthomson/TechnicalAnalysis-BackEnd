package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubEntity;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubFile;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GitHubFileList extends GitHubEntityCollection {
    public GitHubFileList(List<String> commitFiles) {
        super();
        commitFiles.stream()
                .map(GitHubFile::new)
                .forEach(file -> list.put(file.getName(), file));
    }

    @Override
    public GitHubEntity get(String key) {
        return list.get(key);
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
    public void addAll(JsonNode array) {

    }

    @Override
    public void add(GitHubEntity object) {
        GitHubFile commit = (GitHubFile) object;
        this.list.put(commit.getName(), commit);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach((key, value) -> stringBuilder.append(list.get(key)).append("\n"));
        return stringBuilder.toString();
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
