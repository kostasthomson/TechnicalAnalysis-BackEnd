package com.Server.TechnicalAnalysis.Utils.Lists;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GitHubCommitList extends GitHubEntityCollection<GitHubCommit> {
    private final List<Integer> weekIndexes = new ArrayList<>();

    public void clear() {
        weekIndexes.clear();
        super.clear();
    }

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
        this.weekIndexes.add(list.size() - 1); // adding last commit's index as starting reference point
        LocalDate firstDate = LocalDate.parse(list.get(this.weekIndexes.get(0)).getDate(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endOfWeek = firstDate.minusWeeks(1);
        int i = this.weekIndexes.get(0) - 1;
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
        ArrayList<Integer> tdsList = new ArrayList<>();
        for (GitHubCommit commit : list) {
            for (GitHubFile file : commit.getFiles()) {
                if (!file.hasTd()) continue;
                tdsList.add(file.getTd());
            }
        }
        Optional<Integer> max = tdsList.stream().max(Integer::compareTo);
        return max.orElse(-1);
    }

    public void filterFilesWithMetrics() {
        int i = 0;
        while (i < list.size()) {
            GitHubCommit commit = list.get(i);
            commit.filterFilesWithMetrics();
            if (commit.getFiles().isEmpty()) list.remove(i);
            else i++;
        }
    }
}