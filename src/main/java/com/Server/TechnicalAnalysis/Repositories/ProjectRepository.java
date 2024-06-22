package com.Server.TechnicalAnalysis.Repositories;

import com.Server.TechnicalAnalysis.Models.GitHubProject;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends Neo4jRepository<GitHubProject, String> {
    GitHubProject findByName(String name);

    @NotNull
    List<GitHubProject> findAll();
}
