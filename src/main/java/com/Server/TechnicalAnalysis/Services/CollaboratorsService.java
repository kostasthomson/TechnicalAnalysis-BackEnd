package com.Server.TechnicalAnalysis.Services;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;
import com.Server.TechnicalAnalysis.Repositories.CollaboratorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollaboratorsService {
    private final Logger logger = LoggerFactory.getLogger(CollaboratorsService.class);
    @Autowired
    private CollaboratorRepository repository;

    public List<GitHubCollaborator> getAllCollaborators() {
        return this.repository.findAll();
    }

    public GitHubCollaborator getCollaborator(String email) {
        return this.repository.findByEmail(email);
    }

}
