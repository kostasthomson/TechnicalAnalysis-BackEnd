package com.Server.TechnicalAnalysis.Repositories;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "commits", path = "commits")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public interface CommitRepository extends Neo4jRepository<GitHubCommit, String> {
    GitHubCommit findBySha(@Param("sha") String sha);

    @NotNull
    List<GitHubCommit> findAll();
}

