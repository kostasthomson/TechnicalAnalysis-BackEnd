package com.Server.TechnicalAnalysis.Repositories;

import com.Server.TechnicalAnalysis.Models.GitHubProject;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectRepository extends Neo4jRepository<GitHubProject, String> {
    GitHubProject findByName(String name);

    @NotNull
    List<GitHubProject> findAll();
}
