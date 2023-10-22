package com.example.TechnicalAnalysis.Controllers;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Services.GitHubService.RepoClone.GitHubCLI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

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

        // Find the name of the targeted repo
        String[] linkElements = link.split("/");
        String linkRepo = linkElements[linkElements.length - 1];

        // Check if repo exists in directory
        if (!Arrays.stream(Objects.requireNonNull(new File("./ClonedRepos").list())).toList().contains(linkRepo)) {
            System.out.println("Cloning repo");
            GitHubCLI.CloneRepository(link);
        }

        // Write repo's log in file
        GitHubCLI.PrintCommits(linkRepo);


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
