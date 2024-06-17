package com.Server.TechnicalAnalysis.Repositories;


import com.Server.TechnicalAnalysis.Models.GitHubFile;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends Neo4jRepository<GitHubFile, String> {
    List<GitHubFile> findByName(String name);

    @NotNull
    List<GitHubFile> findAll();
}
