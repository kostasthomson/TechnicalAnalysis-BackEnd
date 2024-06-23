package com.Server.TechnicalAnalysis.Services;

import com.Server.TechnicalAnalysis.Models.GitHubProject;
import com.Server.TechnicalAnalysis.Repositories.ProjectRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Value("${sonar.qube.url}")
    private String sonarQubeUrl;
    @Value("${sonar.qube.username}")
    private String sonarQubeUsername;
    @Value("${sonar.qube.password}")
    private String sonarQubePassword;

    @Autowired
    private ProjectRepository repository;
    @Autowired
    private HttpController httpController;

    public List<GitHubProject> getAllProjects() {
        return this.repository.findAll();
    }

    public int getProjectMaxTd(String projectName) {
        return this.repository.findByName(projectName).getMaxTd();
    }

    public int deleteProject(String projectName) {
        HttpResponse<JsonNode> controllerResponse = httpController.postRequest(
                new HttpController.HttpRequest()
                        .setUrl(sonarQubeUrl + "/api/projects/delete")
                        .setParam("project", projectName.replace("/", ":"))
                        .setAuth(sonarQubeUsername, sonarQubePassword)
        );
        if (controllerResponse.getStatus() != 204) return controllerResponse.getStatus();
        repository.deleteByName(projectName.replace(":", "/"));
        return 0;
    }
}
