package com.Server.TechnicalAnalysis.Controllers;


import com.Server.TechnicalAnalysis.Models.GitHubProject;
import com.Server.TechnicalAnalysis.Repositories.ProjectRepository;
import com.Server.TechnicalAnalysis.Services.Analysis.SonarAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectRepository repository;

    @GetMapping
    public List<GitHubProject> getAllProjects() {
        return this.repository.findAll();
    }

    @GetMapping("/max-td")
    public int getProject(@RequestParam("projectName") String name) {
        return this.repository.findByName(name).getMaxTd();
    }
}
