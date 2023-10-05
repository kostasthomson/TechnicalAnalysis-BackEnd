package com.example.TechnicalAnalysis.Controllers;

import com.example.TechnicalAnalysis.Services.ConnectorService.ConnectorController;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.GenericRepository;
import com.example.TechnicalAnalysis.Services.GitHubService.Endpoints.EndpointsUtils.MapKeys;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {
    private final CollaboratorRepository collaboratorRepository;
    private final CommitRepository commitRepository;

    public MainController(CollaboratorRepository colRepo, CommitRepository comRepo) {
        this.collaboratorRepository = colRepo;
        this.commitRepository = comRepo;
    }

    @GetMapping("/init")
    public String InitializeApplication(@RequestParam String link) {
        this.collaboratorRepository.deleteAll();
        this.commitRepository.deleteAll();

        Map<MapKeys, GenericRepository<?, ?>> map = new HashMap<>();
        map.put(MapKeys.COLLABORATORS, collaboratorRepository);
        map.put(MapKeys.COMMITS, commitRepository);

        ConnectorController controller = new ConnectorController(map);

        controller.startAnalyzer();

        return "Set up complete " + link;
    }
}
