package com.example.TechnicalAnalysis;

import com.example.TechnicalAnalysis.Services.ConnectorService.ConnectorController;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCommit;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubEntity;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.GenericRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubCollaboratorList;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseUtils.Collections.GitHubCommitList;
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

            ConnectorController controller = new ConnectorController(new GenericRepository[] {collaboratorRepository, commitRepository});

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
