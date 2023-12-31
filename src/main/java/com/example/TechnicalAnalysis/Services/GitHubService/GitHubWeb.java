package com.example.TechnicalAnalysis.Services.GitHubService;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.GitHubCollaboratorList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public abstract class GitHubWeb {
    private static final String GITHUB_BASE_URL = "https://api.github.com/repos/";
    private static final String GITHUB_OWNER_NAME = "kostasthomson/";
    private static final String GITHUB_REPO_NAME = "BlackJack/";
    private static final String[] GITHUB_ENDPOINTS = new String[]{
            "commits",
            "collaborators"
    };

    public static GitHubCollaboratorList requestCollaborators() {

        GitHubCollaboratorList list = new GitHubCollaboratorList();

        // Load properties from the file
        Properties properties = new Properties();
        try (
                FileInputStream input = new FileInputStream("src/main/resources/application-dev.properties")
        ) {
            properties.load(input);
        } catch (Exception e) {
            return list;
        }

        String apiUrl = GITHUB_BASE_URL + GITHUB_OWNER_NAME + GITHUB_REPO_NAME + GITHUB_ENDPOINTS[GITHUB_ENDPOINTS_INDICES.GITHUB_COLLABORATORS.ordinal()];
        String accessToken = properties.getProperty("apiKey");

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return list;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response.body());
            list.addAll(jsonNode);
        } catch (JsonProcessingException e) {
            return list;
        }

        return list;
    }

    private enum GITHUB_ENDPOINTS_INDICES {
        GITHUB_COMMITS,
        GITHUB_COLLABORATORS
    }
}
