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
                list.put(sha, new GitHubCommit(sha, date, author_id));
            } catch (ParseException e) {
                System.out.println("Wrong Date Format...");
            }
        }
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
