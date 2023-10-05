package com.example.TechnicalAnalysis;

import com.example.TechnicalAnalysis.Services.ConnectorService.ConnectorController;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CollaboratorRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.CommitRepository;
import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Repositories.GenericRepository;
import com.example.TechnicalAnalysis.Services.GitHubService.Endpoints.EndpointsUtils.MapKeys;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

@EnableTransactionManagement
@EnableNeo4jRepositories
@SpringBootApplication
public class TechnicalAnalysisApplication {
    public static String SonarQube = "";
    protected final Log logger = LogFactory.getLog(getClass());

    public static void main(String[] args) {
        SpringApplication.run(TechnicalAnalysisApplication.class, args);
    }

    public static boolean isWindows() {
        return System.getProperty("filename.separator").equals("\\");
    }

    public static void InitializeDB(CollaboratorRepository collaboratorRepository, CommitRepository commitRepository) {
        collaboratorRepository.deleteAll();
        commitRepository.deleteAll();

        Map<MapKeys, GenericRepository<?, ?>> map = new HashMap<>();
        map.put(MapKeys.COLLABORATORS, collaboratorRepository);
        map.put(MapKeys.COMMITS, commitRepository);

        ConnectorController controller = new ConnectorController(map);

        controller.startAnalyzer();

    }

    @Bean
    CommandLineRunner demo(CollaboratorRepository collaboratorRepository, CommitRepository commitRepository) {
        return args -> {
            InitializeDB(collaboratorRepository, commitRepository);
            System.out.println("Set up completed");
        };
    }
}
