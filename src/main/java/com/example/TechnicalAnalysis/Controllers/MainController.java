package com.example.TechnicalAnalysis.Controllers;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Services.GitHubService.GitHubLogReader;
import com.example.TechnicalAnalysis.Services.GitHubService.RepoClone.GitHubCLI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/init")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public class MainController {
    private final CollaboratorRepository collaboratorRepository;
    private final CommitRepository commitRepository;

    @Autowired
    public MainController(CollaboratorRepository colRepo, CommitRepository comRepo) {
        this.collaboratorRepository = colRepo;
        this.commitRepository = comRepo;
    }

    //TODO: Initialization doesn't working
    @GetMapping
    public void InitializeApplication(@RequestParam String link) {
        // Refresh all repositories
        this.collaboratorRepository.deleteAll();
        this.commitRepository.deleteAll();

        // Initialize repository specific attributes
        GitHubCLI.setWorkingRepository(link);

        // Cloning repository
        GitHubCLI.CloneRepository();

        // Write repository's log in file
        GitHubCLI.LogHistory();

        // Read repository's log file
        GitHubLogReader.read();

//        Map<MapKeys, GenericRepository<?, ?>> map = new HashMap<>();
//        map.put(MapKeys.COLLABORATORS, collaboratorRepository);
//        map.put(MapKeys.COMMITS, commitRepository);
//
//        ConnectorController controller = new ConnectorController(map);
//
//        controller.startAnalyzer();
        System.out.println("Set up complete " + link);
    }
}
