package com.Server.TechnicalAnalysis.Utils.Lists;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class GitHubCommitList extends GitHubEntityCollection<GitHubCommit> {
    public void addAll(List<GitHubCommit> commits) {
        list.addAll(commits);
        list.forEach(commit -> map.put(commit.getSha(), list.indexOf(commit)));
    }

    @Override
    public boolean add(GitHubCommit object) {
        map.put(object.getSha(), list.size()-1);
        return list.add(object);
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(((GitHubCommit) o).getSha(), list.indexOf(o)) && list.remove(o);
    }

    public GitHubCommit getLatest() {
        return list.get(list.size()-1);
    }

    public void filterPerWeek() {
        int firstIndex = list.size()-1;
        LocalDate firstDate = LocalDate.parse(list.get(firstIndex).getDate(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endOfWeek = firstDate.minusWeeks(1);
        int i = firstIndex-1;
        LocalDate currentDate;
        while (i >= 0) {
            currentDate = LocalDate.parse(list.get(i).getDate(), DateTimeFormatter.ISO_DATE_TIME);
            if (currentDate.isBefore(endOfWeek) || currentDate.isEqual(endOfWeek)) { // in different week
                List<GitHubCommit> toRemove = list.subList(i + 1, firstIndex);
                toRemove.forEach(commit -> map.remove(commit.getSha()));
                list.removeAll(toRemove);
                firstIndex = i;
                firstDate = currentDate;
                endOfWeek = firstDate.minusWeeks(1);
            }
            i--;
        }

//        HashMap<Integer, GitHubCommit> commitMap = new HashMap<>();
//        HashMap<Integer, LocalDate> weekMap = new HashMap<>();
//        WeekFields weekFields = WeekFields.of(Locale.getDefault());
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            GitHubCommit commit = list.get(entry.getValue());
//            LocalDate dateObj = LocalDate.parse(commit.getDate(), DateTimeFormatter.ISO_DATE_TIME);
//            int week = dateObj.get(weekFields.weekOfWeekBasedYear());
//            LocalDate hashDate = weekMap.putIfAbsent(week, dateObj);
//            if (hashDate == null) {
//                commitMap.put(week, commit);
//                continue;
//            }
//            if (hashDate.isBefore(dateObj)) {
//                weekMap.put(week, dateObj);
//                commitMap.put(week, commit);
//            }
//        }
//        Map<String, GitHubEntity> validCommits = new HashMap<>();
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            GitHubCommit commit = list.get(entry.getValue());
//            if (commitMap.containsValue(commit)) validCommits.put(entry.getKey(), entry.getValue());
//        }
//        list = validCommits;
    }
}