package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "collaborators", path = "collaborators")
//@CrossOrigin(origins = "http://localhost:3000") //enable cors
//public interface CollaboratorRepository extends PagingAndSortingRepository<GitHubCollaborator, String>, CrudRepository<GitHubCollaborator, String> {
//    List<GitHubCollaborator> findByName(@Param("name") String name);
//}
public interface CollaboratorRepository extends GenericRepository <GitHubCollaborator, String> {
}
