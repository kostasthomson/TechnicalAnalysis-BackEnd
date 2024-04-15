package com.example.TechnicalAnalysis.Services.DB;

import com.example.TechnicalAnalysis.Models.GitHubCollaborator;
import com.example.TechnicalAnalysis.Models.GitHubCommit;
import com.example.TechnicalAnalysis.Models.GitHubRepository;
import com.example.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Repositories.FileRepository;
import com.example.TechnicalAnalysis.Repositories.ProjectRepository;
import com.example.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import com.example.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseController {
    @Autowired
    private DatabaseWriter dbWriter; //= new DatabaseWriter();
    @Autowired
    private DatabaseReader dbReader; // = new DatabaseReader();

    private ProjectRepository projectRepository;
    private CollaboratorRepository collaboratorRepository;
    private CommitRepository commitRepository;
    private FileRepository fileRepository;

    public void setProjectRepository(ProjectRepository repo) {
        this.projectRepository = repo;
    }

    public void setCollaboratorRepository(CollaboratorRepository repo) {
        this.collaboratorRepository = repo;
    }

    public void setCommitRepository(CommitRepository repo) {
        this.commitRepository = repo;
    }

    public void setFileRepository(FileRepository repo) {
        this.fileRepository = repo;
    }

    public void createProjectNode(String repoName, GitHubCommit latestCommit) {
        List<GitHubCollaborator> collaboratorList = this.dbReader.getCollaborators(this.collaboratorRepository);
        this.dbWriter.saveRepository(this.projectRepository, new GitHubRepository(repoName, collaboratorList, latestCommit.getComplexity(), latestCommit.getLoc(), latestCommit.getTd()));
    }

    public void writeCommits(GitHubCommitList list) {
        this.dbWriter.saveCommits(this.commitRepository, list);
    }

    public void writeCollaborators(GitHubCollaboratorList list) {
        this.dbWriter.saveCollaborators(this.collaboratorRepository, list);
    }

    public GitHubCollaborator findCollaborator(String name) {
        StringBuilder editedName = new StringBuilder(name.replace(" ", ""));
        GitHubCollaborator collaborator = this.dbReader.findCollaborator(this.collaboratorRepository, editedName.toString());
        if (collaborator == null) {
            collaborator = this.dbReader.findCollaborator(this.collaboratorRepository, editedName.toString().toLowerCase());
        }
        return collaborator;
    }

    public void eraseAll() {
        this.projectRepository.deleteAll();
        this.collaboratorRepository.deleteAll();
        this.commitRepository.deleteAll();
        this.fileRepository.deleteAll();
    }
}
