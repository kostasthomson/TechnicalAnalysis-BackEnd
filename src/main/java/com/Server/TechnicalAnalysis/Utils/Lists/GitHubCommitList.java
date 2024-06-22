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
    private final List<Integer> weekIndexes = new ArrayList<>();

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

    public List<Integer> filterPerWeek() {
        this.weekIndexes.add(list.size()-1); // adding last commit's index as starting reference point
        LocalDate firstDate = LocalDate.parse(list.get(this.weekIndexes.get(0)).getDate(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endOfWeek = firstDate.minusWeeks(1);
        int i = this.weekIndexes.get(0)-1;
        LocalDate currentDate;
        while (i >= 0) {
            currentDate = LocalDate.parse(list.get(i).getDate(), DateTimeFormatter.ISO_DATE_TIME);
            if (currentDate.isBefore(endOfWeek) || currentDate.isEqual(endOfWeek)) { // in different week
                this.weekIndexes.add(0, i); // inserting new week's index
                firstDate = currentDate;
                endOfWeek = firstDate.minusWeeks(1);
            }
            i--;
        }
        return this.weekIndexes;
    }

    public int findMaxFileTd() {
        Optional<Integer> value =  new ArrayList<>(list)
                .stream()
                .map(GitHubCommit::getFiles)
                .map(fList -> fList
                        .stream()
                        .map(GitHubFile::getTd)
                        .filter(Objects::nonNull)
                        .max(Integer::compareTo))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(Integer::compareTo);
        return value.orElse(-1);
    }
}