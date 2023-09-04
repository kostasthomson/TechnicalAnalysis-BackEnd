package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "commits", path = "commits")
//@CrossOrigin(origins = "http://localhost:3000") //enable cors
//public interface CommitRepository extends PagingAndSortingRepository<GitHubCommit, String>, CrudRepository<GitHubCommit, String> {
//    List<GitHubCommit> findBySha(@Param("sha") String sha);
//}
public interface CommitRepository extends GenericRepository <GitHubCommit, String> {
}
