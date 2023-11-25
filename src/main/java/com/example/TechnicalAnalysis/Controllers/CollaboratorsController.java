package com.example.TechnicalAnalysis.Controllers;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collaborators")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public class CollaboratorsController {

    private final CollaboratorRepository repository;

    @Autowired
    public CollaboratorsController(CollaboratorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<GitHubCollaborator> GetAllCommits() {
        return (List<GitHubCollaborator>) this.repository.findAll();
    }

    @GetMapping("/{id}")
    public GitHubCollaborator GetCommit(@PathVariable String id) {
        return this.repository.findByEmail(id);
    }
}
