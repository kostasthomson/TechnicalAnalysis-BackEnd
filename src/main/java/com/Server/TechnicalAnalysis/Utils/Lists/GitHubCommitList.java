package com.Server.TechnicalAnalysis.Utils.Lists;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GitHubCommitList extends GitHubEntityCollection<GitHubCommit> {
    public GitHubCommitList addAll(List<GitHubCommit> commits) {
        list.addAll(commits);
        list.forEach(commit -> map.put(commit.getSha(), list.indexOf(commit)));
        return this;
    }

    @Override
    public boolean add(GitHubCommit object) {
        list.add(object);
        map.put(object.getSha(), list.size() - 1);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(((GitHubCommit) o).getSha(), list.indexOf(o)) && list.remove(o);
    }

    public GitHubCommit getLatest() {
        return list.get(list.size() - 1);
    }

    public void filterPerWeek() {
        int firstIndex = list.size() - 1;
        LocalDate firstDate = LocalDate.parse(list.get(firstIndex).getDate(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endOfWeek = firstDate.minusWeeks(1);
        int i = firstIndex - 1;
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

    public int findMaxFileTd() {
        return new ArrayList<>(list)
                .stream()
                .map(GitHubCommit::getFiles)
                .map(fList -> fList
                        .stream()
                        .map(GitHubFile::getTd)
                        .filter(Objects::nonNull)
                        .max(Integer::compareTo))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(Integer::compareTo)
                .get();
    }
}