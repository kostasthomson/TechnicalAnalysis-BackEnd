package com.Server.TechnicalAnalysis.Repositories;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitRepository extends Neo4jRepository<GitHubCommit, String> {
    GitHubCommit findBySha(String sha);

    @Query("MATCH (c:Commit)-[*2]->(:Project {name: $projectName}) RETURN c")
    List<GitHubCommit> findAllByProjectName(String projectName);

    @NotNull
    List<GitHubCommit> findAll();
}

