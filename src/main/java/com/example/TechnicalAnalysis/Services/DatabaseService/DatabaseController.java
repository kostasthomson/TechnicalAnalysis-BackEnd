package com.example.TechnicalAnalysis.Services.DatabaseService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubCommitList;

public class DatabaseController {
    private static final DatabaseWriter dbWriter = new DatabaseWriter();
    private static final DatabaseReader dbReader = new DatabaseReader();

//    private final Map<MapKeys, GenericRepository<?, ?>> repositories;

//    public DatabaseController(Map<MapKeys, GenericRepository<?, ?>> repos) {
//        this.repositories = repos;
//    }

//    public void write(Map<MapKeys, GitHubEntityCollection> map) {
//        MapKeys key;
//        key = MapKeys.COLLABORATORS;
//        this.dbWriter.saveCollaborators((CollaboratorRepository) this.repositories.get(key), map.get(key));
//        key = MapKeys.COMMITS;
//        this.dbWriter.saveCommits((CommitRepository) this.repositories.get(key), map.get(key));
//    }

    public static void WriteCommits(CommitRepository repo, GitHubCommitList list) {
        dbWriter.saveCommits(repo, list);
    }

    public void read() {
        dbReader.get();
    }
}
