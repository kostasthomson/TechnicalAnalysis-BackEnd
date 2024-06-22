package com.Server.TechnicalAnalysis.Services.Controller;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.Server.TechnicalAnalysis.Repositories.CommitRepository;
import com.Server.TechnicalAnalysis.Repositories.FileRepository;
import com.Server.TechnicalAnalysis.Repositories.ProjectRepository;
import com.Server.TechnicalAnalysis.Services.Analysis.SonarAnalysis;
import com.Server.TechnicalAnalysis.Services.CLI.GitCLI;
import com.Server.TechnicalAnalysis.Services.CLI.GitHubCLI;
import com.Server.TechnicalAnalysis.Services.DB.DatabaseController;
import com.Server.TechnicalAnalysis.Services.Log.GitLogInterpreter;
import com.Server.TechnicalAnalysis.Services.Log.GitLogReader;
import com.Server.TechnicalAnalysis.Services.Web.HttpController;
import com.Server.TechnicalAnalysis.TechnicalAnalysisApplication;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCommitList;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private GitCLI gitCLI;
    @Autowired
    private GitHubCLI gitHubCLI;
    @Autowired
    private GitLogReader gitLogReader;
    @Autowired
    private GitLogInterpreter gitLogInterpreter;
    @Autowired
    private SonarAnalysis sonarAnalysis;
    @Autowired
    private HttpController httpController;

    private int COMMITS_COUNT;
    private String PROJECT_ID;

    private void addRepositories() {
        this.dbController.setProjectRepository(projectRepository);
        this.dbController.setCollaboratorRepository(collaboratorRepository);
        this.dbController.setCommitRepository(commitRepository);
        this.dbController.setFileRepository(fileRepository);
    }

    private void analyzeCommits(GitHubCommitList commits, List<Integer> weekIndeces, String repositoryName, String repositoryOwner) {
        sonarAnalysis.setParams(
                repositoryOwner,
                String.format("%s%s%s", this.gitCLI.getDirectory(), TechnicalAnalysisApplication.PATH_SEPARATOR, repositoryName),
                sonarQubeUrl,
                sonarQubeUsername,
                sonarQubePassword
        );
        for (int i = 0; i < weekIndeces.size(); i++) {
            Integer weekIndex = weekIndeces.get(i);
            GitHubCommit weekCommit = commits.get(weekIndex);
            this.gitHubCLI.addPullRequestTags(weekCommit);
            try {
                Set<GitHubFile> allFiles = new HashSet<>();
                List<GitHubCommit> subList = commits.subList((i == 0) ? 0 : weekIndeces.get(i - 1) + 1, weekIndex + 1);
                for (GitHubCommit subCommit : subList) {
                    allFiles.addAll(subCommit.getFiles());
                }
                sonarAnalysis.analyze(weekCommit, allFiles);
            } catch (IOException | InterruptedException e) {
                this.logger.error("An unexpected exception occurred");
            }
        }
        int succeed = this.gitHubCLI.getSucceed();
        if (succeed != 0) this.logger.warn("Pull request tags: Succeed {}", succeed);
        int failed = this.gitHubCLI.getFailed();
        if (failed != 0) this.logger.warn("Pull request tags: Failed {}", failed);
        this.logger.info("Analyzed commits: {}", weekIndeces.size());
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

    private void resetApplication() {
        this.deleteRepositoryDirectory();
        this.gitLogInterpreter.reset();
    }

    // todo: fix error handling
    // todo: fix analysis on existing projects
    public void startInitialization(String link) {
        // Get repo link information
        String[] splitLink = link.split("/");
        if (splitLink.length <= 1) return;
        String repositoryName = splitLink[splitLink.length - 1];
        String repositoryOwner = splitLink[splitLink.length - 2];
        PROJECT_ID = repositoryOwner + "/" + repositoryName;

        // Add repositories to DatabaseController
        this.addRepositories();

        this.gitCLI.setWorkingRepository(repositoryName);
        this.gitCLI.cloneRepository(link);

        // Request collaborators
        GitHubCollaboratorList collaborators;
        try (BufferedReader bufferedReader = this.gitCLI.LogAuthors()) {
            List<String[]> logAuthors = this.gitLogReader.readCollaborators(bufferedReader);
            collaborators = this.gitLogInterpreter.createCollaboratorList(logAuthors);
        } catch (IOException e) {
            logger.error("IOException Authors error: {}", e.getMessage());
            return;
        }

        // Request commits
        GitHubCommitList commits;
        List<Integer> weekIndexes;
        try (BufferedReader bufferedReader = this.gitCLI.LogCommits()) {
            List<List<String>> commitsLogList = this.gitLogReader.readCommits(bufferedReader);
            commits = this.gitLogInterpreter.createCommitsList(commitsLogList, PROJECT_ID);
            weekIndexes = commits.filterPerWeek();
        } catch (IOException e) {
            logger.error("IOException Commits error: {}", e.getMessage());
            return;
        }

        this.analyzeCommits(commits, weekIndexes, repositoryName, repositoryOwner);

        // Save entities
        this.dbController.writeCollaborators(collaborators);
        this.dbController.createProjectNode(PROJECT_ID, commits.getLatest(), commits.findMaxFileTd());
        this.dbController.writeCommits(commits);

        COMMITS_COUNT = commits.size();

        try {
            File analysisFile = new File(repositoryOwner + "_" + repositoryName + ".csv");
            FileWriter writer = new FileWriter(analysisFile, true);
            // header: project_name; file; package (relative path); sha; tag; contributor; sqale_index; ncloc; code_smells; files; functions; comment_lines; td->td_min
            if (analysisFile.length() == 0)
                writer.append("PROJECT_NAME;FILE;PACKAGE;SHA;TAGS;CONTRIBUTOR;TD;COMPLEXITY;LOC;CODE_SMELLS;FILES;FUNCTIONS;COMMENT_LINES").append("\n");
            for (GitHubCommit commit : commits.stream().filter(GitHubCommit::isWeekCommit).toList()) {
                List<GitHubFile> files = commit.getFiles();
                for (GitHubFile file : files)
                    writer.append(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s",
                            PROJECT_ID,
                            file.getName(),
                            file.getPath(),
                            commit.getSha(),
                            String.join(", ", commit.getTags()),
                            commit.getAuthor().getEmail(),
                            file.getTd(),
                            file.getComplexity(),
                            file.getLoc(),
                            file.getCodeSmells(),
                            file.getNumFiles(),
                            file.getFunctions(),
                            file.getCommentLines()
                    )).append("\n");
            }
            writer.close();
        } catch (IOException e) {
            this.logger.error("Results logging failed");
        }
        // Delete cloned repository directory
        this.resetApplication();
    }

    public int getCOMMITS_COUNT() {
        return this.COMMITS_COUNT;
    }

    public String getPROJECT_ID() {
        return this.PROJECT_ID;
    }
}