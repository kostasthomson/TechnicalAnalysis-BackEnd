package com.Server.TechnicalAnalysis.Controllers;


import com.Server.TechnicalAnalysis.Models.GitHubProject;
import com.Server.TechnicalAnalysis.Repositories.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
