package com.Server.TechnicalAnalysis.Repositories;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitRepository extends Neo4jRepository<GitHubCommit, String> {
    GitHubCommit findBySha(String sha);

    List<GitHubCommit> findAllByProjectName(String projectName);

    @NotNull
    List<GitHubCommit> findAll();
}

