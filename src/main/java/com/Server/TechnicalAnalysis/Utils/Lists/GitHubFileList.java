package com.Server.TechnicalAnalysis.Utils.Lists;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubEntity;
import com.Server.TechnicalAnalysis.Models.GitHubFile;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class GitHubFileList extends GitHubEntityCollection<GitHubFile> {

    public void addAll(List<GitHubFile> list) {
        this.list.addAll(list);
        list.forEach(file -> this.map.put(file.getName(), this.list.indexOf(file)));
    }

    public static List<GitHubFile> parseToList(List<String> commitFiles) {
        List<GitHubFile> files = new ArrayList<>();
        commitFiles.stream()
                .map(GitHubFile::new)
                .filter(GitHubFile::isJava) // Get only java files
                .forEach(files::add);
        return files;
    }

    @Override
    public GitHubFile get(String key) {
        return this.list.get(this.map.get(key));
    }

    @Override
    public boolean add(GitHubFile object) {
        this.map.put(object.getName(), this.list.size()-1);
        return this.list.add(object);
    }

    @Override
    public boolean remove(Object o) {
        return this.map.remove(((GitHubFile) o).getName(), this.list.indexOf(o)) && this.list.remove(o);
    }
}
