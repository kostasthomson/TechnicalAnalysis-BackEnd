package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "collaborators", path = "collaborators")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public interface CollaboratorRepository extends
        PagingAndSortingRepository<GitHubCollaborator, String>,
        CrudRepository<GitHubCollaborator, String> {
    GitHubCollaborator findByName(@Param("name") String name);
}

