package com.example.TechnicalAnalysis;

import com.example.TechnicalAnalysis.GitHub.Controllers.GitHubController;
import com.example.TechnicalAnalysis.GitHub.Collections.GitHubCollaboratorList;
import com.example.TechnicalAnalysis.GitHub.Collections.GitHubCommitList;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCollaborator;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCommit;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import com.example.TechnicalAnalysis.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Repositories.CommitRepository;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableNeo4jRepositories
@SpringBootApplication
public class TechnicalAnalysisApplication {
    protected final Log logger = LogFactory.getLog(getClass());
	public static String SonarQube = "";
	public static void main(String[] args) {
		SpringApplication.run(TechnicalAnalysisApplication.class, args);
	}
	public static boolean isWindows() {
		return System.getProperty("filename.separator").equals("\\");
	}

    @Bean
    CommandLineRunner demo(CollaboratorRepository collaboratorRepository, CommitRepository commitRepository) {
        return args -> {
            collaboratorRepository.deleteAll();
            commitRepository.deleteAll();

            GitHubController controller = new GitHubController();

            GitHubCollaboratorList collaborators = controller.HttpCollaboratorsRequest();

            GitHubCommitList commits = controller.HttpCommitsRequest();
            commits.forEach(controller::HttpCommitRequest);

            controller.CreateRelation(commits, collaborators);

            for (GitHubEntity collaborator: collaborators)
                collaboratorRepository.save((GitHubCollaborator) collaborator);

            for (GitHubEntity commit: commits) {
                commitRepository.save((GitHubCommit) commit);
            }

            System.out.println("Set up completed");
        };
    }
}
