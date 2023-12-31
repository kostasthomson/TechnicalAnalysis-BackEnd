package com.example.TechnicalAnalysis.Controllers;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseController;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.FileRepository;
import com.example.TechnicalAnalysis.Services.GitHubService.GitHubCLI;
import com.example.TechnicalAnalysis.Services.GitHubService.GitHubInterpreter;
import com.example.TechnicalAnalysis.Services.GitHubService.GitHubLogReader;
import com.example.TechnicalAnalysis.Services.GitHubService.GitHubWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/init")
@CrossOrigin(origins = "http://localhost:3000") //enable cors
public class MainController {
    private final CollaboratorRepository collaboratorRepository;
    private final CommitRepository commitRepository;
    private final FileRepository fileRepository;

    @Autowired
    public MainController(CollaboratorRepository colRepo, CommitRepository comRepo, FileRepository fRepo) {
        this.collaboratorRepository = colRepo;
        this.commitRepository = comRepo;
        this.fileRepository = fRepo;
    }

    @GetMapping
    public void InitializeApplication(@RequestParam String link) {
        // Reset all repositories
        this.collaboratorRepository.deleteAll();
        this.commitRepository.deleteAll();
        this.fileRepository.deleteAll();

        DatabaseController.addCommitRepository(commitRepository);
        DatabaseController.addCollaboratorRepository(collaboratorRepository);

        // Initialize repository collaborators
        DatabaseController.WriteCollaborators(GitHubWeb.requestCollaborators());

        // Initialize repository specific attributes
        GitHubCLI.setWorkingRepository(link);

        // Cloning repository
        GitHubCLI.CloneRepository();

        // Write repository's log in file
        GitHubCLI.LogHistory();

        // Read repository's log file
        GitHubLogReader.ReadLogFile();

        // Create commits' list
        GitHubInterpreter.CreateCommitsList();

        // Store commits to repository
        DatabaseController.WriteCommits(GitHubInterpreter.getCommitsList());

        System.out.println("Set up complete " + link);
    }
}
