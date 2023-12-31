package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories;


import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "commits", path = "commits")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public interface FileRepository extends
        PagingAndSortingRepository<GitHubFile, String>,
        CrudRepository<GitHubFile, String> {
    GitHubFile findByName(@Param("name") String name);
}
