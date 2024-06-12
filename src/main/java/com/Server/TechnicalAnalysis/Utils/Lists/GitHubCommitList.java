package com.Server.TechnicalAnalysis.Utils.Lists;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GitHubCommitList extends GitHubEntityCollection<GitHubCommit> {
    public void addAll(List<GitHubCommit> commits) {
        list.addAll(commits);
        list.forEach(commit -> map.put(commit.getSha(), list.indexOf(commit)));
    }

    @Override
    public boolean add(GitHubCommit object) {
        list.add(object);
        map.put(object.getSha(), list.size()-1);
        return true;
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
         List<GitHubCommit> toRemove = list.subList(i + 1, firstIndex);
        toRemove.forEach(commit -> map.remove(commit.getSha()));
        list.removeAll(toRemove);
    }
}