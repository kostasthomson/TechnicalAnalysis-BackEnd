package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "commits", path = "commits")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public interface CommitRepository extends GenericRepository<GitHubCommit, String>, PagingAndSortingRepository<GitHubCommit, String>, CrudRepository<GitHubCommit, String> {
    List<GitHubCommit> findBySha(@Param("sha") String sha);
}

