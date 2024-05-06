package com.Server.TechnicalAnalysis.Services.Web;

import com.Server.TechnicalAnalysis.Enums.GitHubEndpoints;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GitHubWeb {
    private final Logger logger = LoggerFactory.getLogger(GitHubWeb.class);
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String[] githubEndpoints = new String[]{
            "commits",
            "collaborators"
    };
    private final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .header("Accept", "application/vnd.github+json")
            .header("X-GitHub-Api-Version", "2022-11-28");
    private String githubApiUrl;
    @Value("${github.api.key}")
    private String githubApiKey;

    private HttpRequest createRequest(String endpoint) {
        String apiUrl = String.format("%s/%s", this.githubApiUrl, endpoint);
        return this.requestBuilder
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + this.githubApiKey)
                .build();
    }

    public void setURLProperties(String owner, String repo) {
        this.githubApiUrl = String.format("%s/%s/%s", "https://api.github.com/repos", owner, repo);
    }

    private GitHubCollaboratorList requestCollaborators() {
        GitHubCollaboratorList collaboratorList = new GitHubCollaboratorList();
        HttpRequest request = this.createRequest(this.githubEndpoints[GitHubEndpoints.COLLABORATORS.ordinal()]);

        HttpResponse<String> response;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode jsonNode = objectMapper.readTree(response.body());
            collaboratorList.addAll(jsonNode);
        } catch (IOException | InterruptedException e) {
            logger.error("GitHubWeb.requestCollaborators: Error while sending request -> {}", e.getMessage());
        }
        return collaboratorList;
    }

    public GitHubCollaboratorList makeInitialRequest(String repositoryOwner, String repositoryName) {
        this.setURLProperties(repositoryOwner, repositoryName);
        return this.requestCollaborators();
    }
}
