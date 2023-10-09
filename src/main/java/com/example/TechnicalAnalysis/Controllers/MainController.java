package com.example.TechnicalAnalysis.Controllers;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
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
    public String InitializeApplication(@RequestParam String link) {
        this.collaboratorRepository.deleteAll();
        this.commitRepository.deleteAll();

//        Map<MapKeys, GenericRepository<?, ?>> map = new HashMap<>();
//        map.put(MapKeys.COLLABORATORS, collaboratorRepository);
//        map.put(MapKeys.COMMITS, commitRepository);
//
//        ConnectorController controller = new ConnectorController(map);
//
//        controller.startAnalyzer();

        GitHubCLI.CloneRepository(link, null);

        return "Set up complete " + link;
    }
}
