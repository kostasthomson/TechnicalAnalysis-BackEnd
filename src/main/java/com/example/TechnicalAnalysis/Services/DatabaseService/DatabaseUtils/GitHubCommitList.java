package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GitHubCommitList extends GitHubEntityCollection {
    public void addAll(List<GitHubCommit> commits) {
        commits.forEach(commit -> list.put(commit.getSha(), commit));
    }

    public void addAll(JSONArray array) {
        for (Object o : array) {
            JSONObject json = (JSONObject) o;
            String sha = json.get("sha").toString();
            JSONObject commit = (JSONObject) json.get("commit");
            JSONObject author = (JSONObject) json.get("author");
            String author_id = "-1";
            if (author != null) {
                System.out.println(author.toJSONString());
                Long id = (Long) author.get("id");
                if (id != null)
                    author_id = id.toString();
            }
            String date_text = ((JSONObject) commit.get("author")).get("date").toString();
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(date_text);
//                list.put(sha, new GitHubCommit(sha, author_id));
            } catch (ParseException e) {
                System.out.println("Wrong Date Format...");
            }
        }
    }

    @Override
    public void addAll(JsonNode array) {

    }

    @Override
    public void add(GitHubEntity object) {
        GitHubCommit commit = (GitHubCommit) object;
        this.list.put(commit.getSha(), commit);
    }

    public GitHubCommit get(String sha) {
        try {
            return (GitHubCommit) this.list.get(sha);
        } catch (Exception ignored) {
            System.out.println("No commit match");
        }
        return null;
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