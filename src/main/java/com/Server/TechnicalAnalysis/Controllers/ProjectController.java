package com.Server.TechnicalAnalysis.Controllers;


import com.Server.TechnicalAnalysis.Models.GitHubProject;
import com.Server.TechnicalAnalysis.Services.ProjectService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private ProjectService service;

    @GetMapping
    public List<GitHubProject> getAllProjects() {
        return this.service.getAllProjects();
    }

    @GetMapping("/max-td")
    public int getProject(@RequestParam String projectName) {
        return this.service.getProjectMaxTd(projectName);
    }

    @DeleteMapping("/delete")
    public void deleteProject(@RequestParam String projectName, HttpServletResponse response) {
        int status = this.service.deleteProject(projectName);
        if (status != 0) response.setStatus(status);
        else response.setStatus(200);
    }
}
