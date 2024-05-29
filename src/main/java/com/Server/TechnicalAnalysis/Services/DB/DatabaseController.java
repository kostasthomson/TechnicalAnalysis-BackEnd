package com.Server.TechnicalAnalysis.Services.DB;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;
import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubRepository;
import com.Server.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.Server.TechnicalAnalysis.Repositories.CommitRepository;
import com.Server.TechnicalAnalysis.Repositories.FileRepository;
import com.Server.TechnicalAnalysis.Repositories.ProjectRepository;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
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

    public GitHubCollaborator findCollaborator(String key) {
        return this.dbReader.findCollaborator(this.collaboratorRepository, key.replace(" ", ""));
    }

    public void eraseAll() {
        this.projectRepository.deleteAll();
        this.collaboratorRepository.deleteAll();
        this.commitRepository.deleteAll();
        this.fileRepository.deleteAll();
    }
}
