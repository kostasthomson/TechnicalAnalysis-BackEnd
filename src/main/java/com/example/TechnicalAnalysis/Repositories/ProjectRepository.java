package com.example.TechnicalAnalysis.Repositories;

import com.example.TechnicalAnalysis.Models.GitHubRepository;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "repositories", path = "repositories")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public interface ProjectRepository extends Neo4jRepository<GitHubRepository, String> {
    GitHubRepository findByName(@Param("name") String name);
}
