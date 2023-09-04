package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GitHubCommitList extends GitHubEntityCollection {
    public void addAll(JSONArray array) {
        for (Object o : array) {
            JSONObject json = (JSONObject) o;
            String sha = json.get("sha").toString();
            JSONObject commit = (JSONObject) json.get("commit");
            JSONObject author = (JSONObject) commit.get("author");
            String date_text = author.get("date").toString();
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(date_text);
                list.add(new GitHubCommit(sha, date));
            } catch (ParseException e) {
                System.out.println("Wrong Date Format...");
            }
        }
    }

    public GitHubCommit get(String sha) {
        for (GitHubEntity entity : list) {
            GitHubCommit commit = (GitHubCommit) entity;
            if (commit.Is(sha)) {
                return commit;
            }
        }
        throw new RuntimeException("No commit match");
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
