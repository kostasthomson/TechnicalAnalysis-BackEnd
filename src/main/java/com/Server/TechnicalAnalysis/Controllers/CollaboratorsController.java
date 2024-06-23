package com.Server.TechnicalAnalysis.Controllers;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;
import com.Server.TechnicalAnalysis.Services.CollaboratorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/collaborators")
public class CollaboratorsController {

    @Autowired
    private CollaboratorsService service;

    @GetMapping
    public List<GitHubCollaborator> getAllCollaborators() {
        return this.service.getAllCollaborators();
    }

    @GetMapping("/{email}")
    public GitHubCollaborator getCollaborator(@PathVariable String email) {
        return this.service.getCollaborator(email);
    }
}
