package com.example.TechnicalAnalysis.Repositories;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCollaborator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "collaborators", path = "collaborators")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public interface CollaboratorRepository extends PagingAndSortingRepository<GitHubCollaborator, Long>, CrudRepository<GitHubCollaborator, Long> {
    List<GitHubCollaborator> findByName(@Param("name") String name);
}
