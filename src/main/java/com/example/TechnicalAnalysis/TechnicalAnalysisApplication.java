package com.example.TechnicalAnalysis;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
}
