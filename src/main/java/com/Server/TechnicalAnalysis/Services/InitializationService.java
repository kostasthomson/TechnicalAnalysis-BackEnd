package com.Server.TechnicalAnalysis.Services;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Models.GitHubProject;
import com.Server.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.Server.TechnicalAnalysis.Repositories.CommitRepository;
import com.Server.TechnicalAnalysis.Repositories.FileRepository;
import com.Server.TechnicalAnalysis.Repositories.ProjectRepository;
import com.Server.TechnicalAnalysis.Services.CLI.GitCliService;
import com.Server.TechnicalAnalysis.Services.CLI.GitHubCliService;
import com.Server.TechnicalAnalysis.Services.Log.GitLogInterpreterService;
import com.Server.TechnicalAnalysis.Services.Log.GitLogReaderService;
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
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private GitCliService gitCLI;
    @Autowired
    private GitHubCliService gitHubCLI;
    @Autowired
    private GitLogReaderService gitLogReader;
    @Autowired
    private GitLogInterpreterService gitLogInterpreter;
    @Autowired
    private SonarAnalysisService sonarAnalysis;
    @Autowired
    private HttpControllerService httpController;

    private int ANALYZED_COMMITS;
    private int PROJECT_COMMITS;
    private int FILTERED_COMMITS;
    private String PROJECT_ID;

    private HashMap<String, List<GitHubFile>> createMapForFiles(GitHubCommitList commits, List<Integer> weekIndexes, int i, int weekIndex) {
        HashMap<String, List<GitHubFile>> files = new HashMap<>();
        List<GitHubCommit> subList = commits.subList((i == 0) ? 0 : weekIndexes.get(i - 1) + 1, weekIndex + 1);
        for (GitHubCommit subCommit : subList) {
            for (GitHubFile file : subCommit.getFiles()) {
                List<GitHubFile> stored = files.getOrDefault(file.getPath(), new ArrayList<>());
                stored.add(file);
                files.put(file.getPath(), stored);
            }
        }
        return files;
    }

    private void analyzeCommits(GitHubCommitList commits, List<Integer> weekIndexes) {
        for (int i = 0; i < weekIndexes.size(); i++) {
            Integer weekIndex = weekIndexes.get(i);
            GitHubCommit weekCommit = commits.get(weekIndex);
            this.gitHubCLI.addPullRequestTags(weekCommit);
            try {
                HashMap<String, List<GitHubFile>> allFiles = this.createMapForFiles(commits, weekIndexes, i, weekIndex);
                sonarAnalysis.analyze(weekCommit, allFiles);
                this.logger.info("Analyzed {} / {} weeks", i + 1, weekIndexes.size());
            } catch (IOException | InterruptedException e) {
                this.logger.error("An unexpected exception occurred");
            }
        }
        int succeed = this.gitHubCLI.getSucceed();
        if (succeed != 0) this.logger.warn("Pull request tags: Succeed {}", succeed);
        int failed = this.gitHubCLI.getFailed();
        if (failed != 0) this.logger.warn("Pull request tags: Failed {}", failed);
    }

    private void clearRepositoryDirectory() {
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
        this.clearRepositoryDirectory();
        this.gitLogInterpreter.reset();
    }

    public void startInitialization(String link) {
        Instant start = Instant.now();

        // Get repo link information
        String[] splitLink = link.split("/");
        if (splitLink.length <= 1) return;
        String repositoryName = splitLink[splitLink.length - 1];
        String repositoryOwner = splitLink[splitLink.length - 2];
        this.PROJECT_ID = repositoryOwner + "/" + repositoryName;

        this.gitCLI.setWorkingRepository(repositoryName);
        this.sonarAnalysis.setParams(
                repositoryOwner,
                repositoryName,
                sonarQubeUrl,
                sonarQubeUsername,
                sonarQubePassword
        );

        // clone repo
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
            this.PROJECT_COMMITS = commits.size();
            weekIndexes = commits.filterPerWeek();
            this.ANALYZED_COMMITS = weekIndexes.size();
        } catch (IOException e) {
            logger.error("IOException Commits error: {}", e.getMessage());
            return;
        }


        try {
            // analyze repo
            this.analyzeCommits(commits, weekIndexes);
            // filter commits
            commits.filterFilesWithMetrics();

            this.FILTERED_COMMITS = commits.size();
            this.logger.warn("unfiltered: {} | week: {} | filtered {}", this.PROJECT_COMMITS, this.ANALYZED_COMMITS, this.FILTERED_COMMITS);

            // Save entities
            this.collaboratorRepository.saveAll(collaborators);
            GitHubCommit latestCommit = commits.getLatest();
            this.projectRepository.save(new GitHubProject(latestCommit.getProjectName(), collaborators, latestCommit, commits.findMaxFileTd()));
            this.commitRepository.saveAll(commits);

            File analysisFile = new File(repositoryOwner + "_" + repositoryName + ".csv");
            FileWriter writer = new FileWriter(analysisFile, true);
            // header: project_name; file; package (relative path); sha; tag; contributor; sqale_index; ncloc; code_smells; files; functions; comment_lines; td->td_min
            if (analysisFile.length() == 0)
                writer.append("PROJECT_NAME;FILE;PACKAGE;SHA;IS_WEEK;CONTRIBUTOR;TAGS;TD;COMPLEXITY;LOC;CODE_SMELLS;FILES;FUNCTIONS;COMMENT_LINES").append("\n");
            for (GitHubCommit commit : commits) {
                List<GitHubFile> files = commit.getFiles();
                for (GitHubFile file : files)
                    writer.append(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s",
                            PROJECT_ID,
                            file.getName(),
                            file.getPath(),
                            commit.getSha(),
                            commit.isWeekCommit(),
                            commit.getAuthor().getEmail(),
                            String.join(", ", commit.getTags()),
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

            // Delete cloned repository directory
            this.addLogResults(start, Instant.now(), link);
        } catch (IOException e) {
            this.logger.error("Results logging failed");
        } catch (Exception e) {
            this.logger.error("Unexpected error: {}", e.getMessage());
        } finally {
            this.resetApplication();
        }
    }

    private void addLogResults(Instant start, Instant end, String link) {
        Duration elapsedTime = Duration.between(start, end);
        long fullSeconds = elapsedTime.getSeconds();
        long hours = fullSeconds / 3600;
        long minutes = fullSeconds % 3600 / 60;
        long seconds = fullSeconds % 60;
        this.logger.info("Execution time -> {}:{}:{}", hours, minutes, seconds);
        this.logger.info("Set up complete {}", link);
        try {
            File logFile = new File("./RESULTS_LOG.log");
            FileWriter writer = new FileWriter(logFile, true);
            // header: timestamp, projectId, commits, timeElapsed
            if (logFile.length() == 0)
                writer.append("TIMESTAMP, PROJECT, PROJECT_COMMITS, ANALYZED_COMMITS, FILTERED_COMMITS, INIT_TIME").append("\n");
            writer.append(String.format("%s,%s,%s,%s,%s,%s:%s:%s",
                    new Timestamp(System.currentTimeMillis()),
                    this.PROJECT_ID,
                    this.PROJECT_COMMITS,
                    this.ANALYZED_COMMITS,
                    this.FILTERED_COMMITS,
                    hours,
                    minutes,
                    seconds
            )).append("\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
