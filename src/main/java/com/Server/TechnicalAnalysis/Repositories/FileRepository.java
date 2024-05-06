package com.Server.TechnicalAnalysis.Repositories;


import com.Server.TechnicalAnalysis.Models.GitHubFile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "commits", path = "commits")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public interface FileRepository extends Neo4jRepository<GitHubFile, String> {
    GitHubFile findByName(@Param("name") String name);
}
