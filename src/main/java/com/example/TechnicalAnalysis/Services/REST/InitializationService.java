package com.example.TechnicalAnalysis.Services.REST;

import com.example.TechnicalAnalysis.Enums.AnalysisMetrics;
import com.example.TechnicalAnalysis.Models.GitHubCommit;
import com.example.TechnicalAnalysis.Models.GitHubEntity;
import com.example.TechnicalAnalysis.Models.GitHubFile;
import com.example.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Repositories.FileRepository;
import com.example.TechnicalAnalysis.Repositories.ProjectRepository;
import com.example.TechnicalAnalysis.Services.CLI.GitCLI;
import com.example.TechnicalAnalysis.Services.DB.DatabaseController;
import com.example.TechnicalAnalysis.Services.Log.GitLogInterpreter;
import com.example.TechnicalAnalysis.Services.Log.GitLogReader;
import com.example.TechnicalAnalysis.Services.Web.GitHubWeb;
import com.example.TechnicalAnalysis.Utils.Analysis.SonarAnalysis;
import com.example.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import com.example.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class InitializationService {
    private final Logger logger = LoggerFactory.getLogger(InitializationService.class);

    @Value("${sonar.qube.url}")
    private String sonarQubeUrl;
    @Value("${sonar.qube.username}")
    private String sonarQubeUsername;
    @Value("${sonar.qube.password}")
    private String sonarQubePassword;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CollaboratorRepository collaboratorRepository;
    @Autowired
    private CommitRepository commitRepository;
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private DatabaseController dbController;
    @Autowired
    private GitHubWeb gitHubWeb;
    @Autowired
    private GitCLI gitCLI;
    @Autowired
    private GitLogReader gitLogReader;
    @Autowired
    private GitLogInterpreter gitLogInterpreter;

    private void addRepositories() {
        this.dbController.setProjectRepository(projectRepository);
        this.dbController.setCollaboratorRepository(collaboratorRepository);
        this.dbController.setCommitRepository(commitRepository);
        this.dbController.setFileRepository(fileRepository);
        this.dbController.eraseAll();
    }

    private void analyzeCommits(GitHubCommitList commits, String repositoryName) {
        for (GitHubEntity ghe : commits) {
            GitHubCommit commit = (GitHubCommit) ghe;
            try {
                SonarAnalysis sonarAnalysis = new SonarAnalysis(
                        "kostasthomson",
                        "ClonedRepos\\" + repositoryName,
                        commit.getSha(),
                        sonarQubeUrl,
                        sonarQubeUsername,
                        sonarQubePassword
                );
                commit.setComplexity(sonarAnalysis.getComplexity());
                commit.setTd(sonarAnalysis.getTD());
                commit.setLoc(sonarAnalysis.getLOC());
                List<GitHubFile> files = commit.getFiles();
                for (GitHubFile file : files) {
                    try {
                        Map<AnalysisMetrics, Integer> metrics = sonarAnalysis.getFileMetricFromSonarQube(file.getName());
                        file.setComplexity(metrics.get(AnalysisMetrics.COMPLEXITY));
                        file.setTd(metrics.get(AnalysisMetrics.TD));
                        file.setLoc(metrics.get(AnalysisMetrics.LOC));
                    } catch (NullPointerException e) {
                        this.logger.error("startInitialization: {}", e.getMessage());
                    }
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void deleteRepositoryDirectory() {
        int i = 3;
        while (i-- != 0) {
            try {
                FileUtils.cleanDirectory(new File("ClonedRepos"));
                break;
            } catch (IOException e) {
                this.logger.warn("startInitialization: Repository directory deletion failed");
            }
        }
    }

    public void startInitialization(String link) {
        // Add repositories to DatabaseController
        this.addRepositories();

        // Get repo link information
        String[] splitLink = link.split("/");
        String repositoryName = splitLink[splitLink.length - 1];
        String repositoryOwner = splitLink[splitLink.length - 2];

        // Request collaborators
        GitHubCollaboratorList collaborators = this.gitHubWeb.makeInitialRequest(repositoryOwner, repositoryName);
        // Save collaborators
        this.dbController.writeCollaborators(collaborators);

        // Request commits
        File commitsLogFile = this.gitCLI.createLog(link);
        List<List<String>> commitsLogList = this.gitLogReader.readLogFile(commitsLogFile);
        GitHubCommitList commits = this.gitLogInterpreter.createCommitsList(commitsLogList);

        // Analyze commits
        this.analyzeCommits(commits, repositoryName);

        // Save Project entity and commits
        this.dbController.createProjectNode(repositoryName, commits.getLatest());
        this.dbController.writeCommits(commits);

        // Delete cloned repository directory
        this.deleteRepositoryDirectory();
    }
}
