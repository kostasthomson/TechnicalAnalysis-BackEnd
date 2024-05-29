package com.Server.TechnicalAnalysis.Services.REST;

import com.Server.TechnicalAnalysis.Enums.AnalysisMetrics;
import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubEntity;
import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.Server.TechnicalAnalysis.Repositories.CommitRepository;
import com.Server.TechnicalAnalysis.Repositories.FileRepository;
import com.Server.TechnicalAnalysis.Repositories.ProjectRepository;
import com.Server.TechnicalAnalysis.Services.CLI.GitCLI;
import com.Server.TechnicalAnalysis.Services.CLI.GitHubCLI;
import com.Server.TechnicalAnalysis.Services.DB.DatabaseController;
import com.Server.TechnicalAnalysis.Services.Log.GitLogInterpreter;
import com.Server.TechnicalAnalysis.Services.Log.GitLogReader;
import com.Server.TechnicalAnalysis.Services.Web.GitHubWeb;
import com.Server.TechnicalAnalysis.Utils.Analysis.SonarAnalysis;
import com.Server.TechnicalAnalysis.Utils.GitHubCollaboratorBuilder;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
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
    private GitHubCLI gitHubCLI;
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

    private void analyzeCommits(GitHubCommitList commits, String repositoryName, String repositoryOwner) {
        for (GitHubCommit commit : commits) {
            this.gitHubCLI.addPullRequestTags(commit);
            try {
                SonarAnalysis sonarAnalysis = new SonarAnalysis(
                        repositoryOwner,
                        String.format("%s\\%s", this.gitCLI.getDirectory(), repositoryName),
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
                        Map<AnalysisMetrics, Integer> metrics = sonarAnalysis.getFileMetricFromSonarQube(file.getPath());
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
                FileUtils.cleanDirectory(new File("Repositories"));
                break;
            } catch (IOException e) {
                this.logger.warn("startInitialization: Repository directory deletion failed");
            }
        }
    }
    // todo: fix error handling
    // todo: fix analysis on existing projects
    public void startInitialization(String link) {
        // Add repositories to DatabaseController
        this.addRepositories();

        // Get repo link information
        String[] splitLink = link.split("/");
        String repositoryName = splitLink[splitLink.length - 1];
        String repositoryOwner = splitLink[splitLink.length - 2];

        this.gitCLI.setWorkingRepository(repositoryName);
        this.gitCLI.cloneRepository(link);

        // Request collaborators
        File f = this.gitCLI.LogAuthors();
        List<String[]> logAuthors = this.gitLogReader.readCollaborators(f);
        GitHubCollaboratorList collaborators = this.gitLogInterpreter.createCollaboratorList(logAuthors);

        // Request commits
        File commitsLogFile = this.gitCLI.LogCommits();
        List<List<String>> commitsLogList = this.gitLogReader.readCommits(commitsLogFile);
        GitHubCommitList commits = this.gitLogInterpreter.createCommitsList(commitsLogList);
        commits.filterPerWeek();

        this.analyzeCommits(commits, repositoryName, repositoryOwner);

        // Save entities
        this.dbController.writeCollaborators(collaborators);
        this.dbController.createProjectNode(repositoryName, commits.getLatest());
        this.dbController.writeCommits(commits);

        // Delete cloned repository directory
        this.deleteRepositoryDirectory();
    }
}
