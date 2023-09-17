package com.example.TechnicalAnalysis.Controllers;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public class CollaboratorsController {

    private final CollaboratorRepository repository;

    public CollaboratorsController(CollaboratorRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/collaborators")
    public List<GitHubCollaborator> GetAllCommits() {
        return (List<GitHubCollaborator>) this.repository.findAll();
    }

    @GetMapping("/collaborators/{id}")
    public GitHubCollaborator GetCommit(@PathVariable long id) {
        return this.repository.findById(id);
    }
}
