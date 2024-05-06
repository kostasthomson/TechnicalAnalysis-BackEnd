package com.Server.TechnicalAnalysis.Repositories;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "collaborators", path = "collaborators")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public interface CollaboratorRepository extends Neo4jRepository<GitHubCollaborator, String> {
    GitHubCollaborator findByName(@Param("name") String name);

    @NotNull
    List<GitHubCollaborator> findAll();
}

